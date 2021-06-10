package net.superdark.minecraft.plugins.SuperDarkCore.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CommandDelegate
{
    /**
     * This creates a handler for a given method. The method should satify all these conditions:
     * * Be a public static method
     * * Have a CommandHandler annotation
     * * The parameters of the method should match the expected CommandHandler path.
     *
     * If they don't then this constructor with throw the listed exception.
     * @param annotation The annotation for this method
     * @param m The method to check/bind to
     * @exception IllegalArgumentException if the method does not statify the listed requirements
     */
    public CommandDelegate(CommandHandler annotation, Method m)
    {
        if(!Modifier.isStatic(m.getModifiers()))
        {
            throwEx("Given method is not static!");
        }

        if(annotation == null)
        {
            throwEx("annotation was null?");
        }

        if(!CommandReflection.DoesMethodMeetPathReq(annotation.Path(), m))
        {
            throwEx("Given method does not meet path requirements");
        }

        _annotation = annotation;
        _m = m;
    }

    /**
     * Invoke this delegate with the parameters given. This handler will catch all exceptions that the invoked method
     * will cast.
     * @param parameters The parameters to invoke the method with.
     */
    public void invoke(Object[] parameters)
    {
        try
        {
            _m.invoke(null, parameters);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @return The annotation that this delegate bound to
     */
    public CommandHandler getAnnotation()
    {
        return _annotation;
    }

    /**
     * Convenient handler for us to throw an exception
     * @param msg The message to give to the exception
     * @throws IllegalArgumentException with the given message msg.
     */
    private void throwEx(String msg)
    {
        throw new IllegalArgumentException(msg);
    }

    private final CommandHandler _annotation;
    private final Method _m;
}
