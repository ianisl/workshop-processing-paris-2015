import java.util.Calendar;

// Cette fonction génère un timestamp (information sur la date et l'heure d'exécution)
// sous la forme 2015-04-05_20h-30m-22s
String getTimestamp() 
{
    Calendar now = Calendar.getInstance();
    return String.format("20%1$ty-%1$tm-%1$td_%1$tHh%1$tMm%1$tSs", now);
}
