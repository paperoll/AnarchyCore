package org.wksh.core.common.event;

public class Event
{
    private boolean cancelled;

    public boolean cancelled()
    {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }
}
