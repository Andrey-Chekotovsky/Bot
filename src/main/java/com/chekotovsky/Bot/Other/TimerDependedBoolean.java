package com.chekotovsky.Bot.Other;

import lombok.Getter;
import lombok.Setter;


public class TimerDependedBoolean {

    private boolean value;
    private long lastActivation = 0L;
    @Getter
    @Setter
    private long timePeriodInMillis = 0L;

    public TimerDependedBoolean(boolean startValue) {
        this.value = startValue;
    }

    public void setValue(boolean value) {
        this.value = value;
        this.lastActivation = System.currentTimeMillis();
    }

    public boolean getValue() {
        long currentTime = System.currentTimeMillis();
        if (lastActivation > currentTime)
        {
            this.setValue(true);
        }
        else if (currentTime - lastActivation > timePeriodInMillis) {
            this.setValue(true);
        }
        return this.value;
    }
}
