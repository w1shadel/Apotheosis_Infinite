<<<<<<<< HEAD:src/main/java/com/maxwell/apotheosis_morepower/AFP.java
package com.maxwell.apotheosis_morepower;
========
package com.maxwell.unholy_tale;
>>>>>>>> 149509186f6907b5a53a0e60444f3786bb86c4b6:src/main/java/com/maxwell/unholy_tale/Unholy.java

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
<<<<<<<< HEAD:src/main/java/com/maxwell/apotheosis_morepower/AFP.java
@Mod(AFP.MODID)
public class AFP
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "apotheosis_morepower";
    public AFP(FMLJavaModLoadingContext context)
========
@Mod(Unholy.MODID)
public class Unholy
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "unholy_tale";
    public Unholy(FMLJavaModLoadingContext context)
>>>>>>>> 149509186f6907b5a53a0e60444f3786bb86c4b6:src/main/java/com/maxwell/unholy_tale/Unholy.java
    {
        IEventBus modEventBus = context.getModEventBus();
    }
}
