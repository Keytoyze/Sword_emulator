package indi.key.mipsemulator.core.controller;

import java.util.HashSet;
import java.util.Set;

import indi.key.mipsemulator.model.interfaces.TickCallback;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.util.Duration;

public class TimingRenderer {

    private static ScheduledService<Void> scheduledService;

    static {
        scheduledService = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        for (TickCallback tickCallback : callbackSet) {
                            try {
                                tickCallback.onTick();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                };
            }
        };
        scheduledService.setPeriod(Duration.millis(40));
    }

    private static Set<TickCallback> callbackSet = new HashSet<>();

    public static void register(TickCallback tickCallback) {
        callbackSet.add(tickCallback);
        if (callbackSet.size() != 0) {
            start();
        }
    }

    public static void unRegister(TickCallback tickCallback) {
        callbackSet.remove(tickCallback);
        if (callbackSet.size() == 0) {
            stop();
        }
    }

    private static void start() {
        if (scheduledService.getState() != Worker.State.READY) {
            scheduledService.restart();
        } else {
            scheduledService.start();
        }
    }

    private static void stop() {
        scheduledService.cancel();
    }
}
