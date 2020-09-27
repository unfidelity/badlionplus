# Badlion+

-Once private client gone public for a couple reasons I'll state below.
-Don't expect amazing and flawless coding practices. This is my first attempt at a (semi) custom base client and is only backed by about 4 months of Java. 
-You're likely going to see code that makes no sense, spaghetti code, and an unreasonable amount of try{}catch(){} brackets to prevent crashes in some modules.
-If anyone more competent than me would like to make a pull request, please do. I plan on actively updating the client so your work wouldn't go to waste. 

### How to use:

#### For normal people

-Download the .jar from either the releases or my discord [https://discord.gg/CyDwds3] (both will be the same)
-Place in mods folder
-Run 1.12.2 Forge
-Enjoy

#### For very cool developers

-Download the source or fork and download.
-If you are using windows, open cmd and type `cd <drag directory folder in>` and hit enter
-Run `gradlew setupDecompWorkspace`.
-Run `gradlew eclipse` if you are using eclipse, or `gradlew idea` if you are using Intellij.
-You may also have to run `gradlew genIdeaRuns` or something like that if you use Intellij (I use eclipse)
-Open project in your desired IDE

-If you are using Mac/Linux, open Terminal.
-Type `cd <drag directory folder in>` and hit enter
-Type `chmod +x ./gradlew` and hit enter
-Run `./gradlew setupDecompWorkspace`.
-Run `./gradlew eclipse` if you are using eclipse, or `./gradlew idea` if you are using Intellij.
-You may also have to run `./gradlew genIdeaRuns` or something like that if you use Intellij (I use eclipse)
-Open project in your desired IDE

### For building yourself

-Open Terminal / CMD
-Change Directory (or cd) to the project folder you downloaded by typing `cd <drag in the folder from your desktop/downloads (or wherever it is)>` and hit enter
-If you are on Mac/Linux, type `chmod +x ./gradlew`. If you are on Windows, skip this step.
-If you are on Mac/Linux, type `./gradlew build` and hit enter. If you are on Windows, type `gradlew build` and hit enter.
-The built jar will appear in /build/libs/ of the project folder you initially cd'ed to. Use the larger jar, not the -sources jar.

### Notable things

-The long awaited (working) BedAura for 1.13+ servers
-And a variety of other unique modules that I thought were pretty cool including packet autocity and selfanvil
-Please note that most of the pvp modules are designed for 1.13+ servers such as ECME. They rely on a lot of the block checks removed in 1.13+, such as airplace and the ability to place crystals in one block spaces (which makes the autocity very effective)

### Why?

-I released this because most of the people who originally had it are no longer active and being the first person to release a working bedaura would probably be nice for clout
-I would like to use this client as an opportunity to recreate modules that people might see in private ones so everyone can use them too
-I would also like to see how awful spawn fights are going to look like with everyone running around with bedaura

### Credits

-Of course not everything here is fully custom. Credit to HeroCode/Osiris+ for some parts of the module managers, command managers, settings system, and GUI base.
-Credit to Salhack, Phobos, Kami, and a few others for inspiration for a few modules
