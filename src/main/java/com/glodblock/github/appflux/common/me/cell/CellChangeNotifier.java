package com.glodblock.github.appflux.common.me.cell;

import ae2.api.stacks.AEKey;
import ae2.api.storage.MEStorageChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Listener bookkeeping shared by the flux cell inventories.
 * <p>
 * Storage cells must publish every content change as a signed delta after the initial enumeration, and must reject
 * stale listeners through {@link MEStorageChangeListener#isValid(Object)} before every callback.
 */
final class CellChangeNotifier {

    private final List<Registration> listeners = new ArrayList<>();

    void addListener(MEStorageChangeListener listener, Object verificationToken) {
        Objects.requireNonNull(listener, "listener");
        for (Registration registration : this.listeners) {
            if (registration.listener == listener) {
                throw new IllegalStateException("The storage listener is already registered.");
            }
        }
        this.listeners.add(new Registration(listener, verificationToken));
    }

    void removeListener(MEStorageChangeListener listener) {
        this.listeners.removeIf(registration -> registration.listener == listener);
    }

    /**
     * Publishes a signed content change to every still valid listener.
     *
     * @param what  resource whose amount changed
     * @param delta signed amount added to or removed from the storage
     */
    void onStackChange(AEKey what, long delta) {
        if (delta == 0) {
            return;
        }
        for (Registration registration : new ArrayList<>(this.listeners)) {
            if (!this.listeners.contains(registration)) {
                // Removed by a previous callback of this same dispatch.
                continue;
            }
            if (!registration.listener.isValid(registration.verificationToken)) {
                this.listeners.remove(registration);
                continue;
            }
            registration.listener.onStackChange(what, delta);
        }
    }

    private record Registration(MEStorageChangeListener listener, Object verificationToken) {
    }
}
