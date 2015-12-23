-libraryjars <java.home>/lib/rt.jar
-libraryjars <java.home>/lib/jce.jar

-dontwarn javax.servlet.*

-dontwarn org.apache.**

-keep public class de.kuschku.extract.ExtractorHandler

-dontobfuscate
