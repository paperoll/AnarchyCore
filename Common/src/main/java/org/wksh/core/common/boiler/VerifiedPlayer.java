package org.wksh.core.common.boiler;

import java.util.UUID;

public class VerifiedPlayer
{
    private static String name;
    private static UUID uniqueId;

    public VerifiedPlayer(String name, UUID uniqueId)
    {
        VerifiedPlayer.name = name;
        VerifiedPlayer.uniqueId = uniqueId;
    }

    public static String name()
    {
        return name;
    }

    public static UUID uniqueId()
    {
        return uniqueId;
    }
}
