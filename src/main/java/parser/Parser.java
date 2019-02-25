package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class Parser{
    protected final static String AFISHA_URL = "https://www.afisha.ru";

    /**
     *
     * @param afisha
     * @return
     */
    public abstract Elements[] getElems(Document afisha);

    /**
     *
     * @param classInfo элементы, в которых описывается местоположение (elms[1])
     * @param index местоположение элемента в списке
     * @return возвращает имя локации
     */
    //Как назвать classInfo?
    public abstract String getLocation(Elements classInfo, int index);

    public static Document getDocument(String url) throws IOException{

        Connection con = Jsoup.connect(url);
        Document afisha = con.get();
        return afisha;
    }
    public abstract void setTime(Event event, Element e);

    public abstract String getTypeOfEvent();


    public ArrayList<Event> parse(Document afisha){
        ArrayList<Event> events = new ArrayList<>();
        String eventUrl, eventName, location, description, date_start, date_end;
        String type_of_event = getTypeOfEvent();
        Elements[] elems = getElems(afisha);

        //elems[0] содержит имена и урл
        //elms[1] содержит описание и местоположение
        //elms[2] содержит информацию о времени проведения
        for (int i = 0; i < elems[0].size(); i++ ){

            if(!type_of_event.equals("theatre")) eventUrl = AFISHA_URL + elems[0].get(i).attr("href");
                else eventUrl = AFISHA_URL  + elems[0].get(i).child(0).attr("href");
            eventName = elems[0].get(i).text();
            if(type_of_event.equals("cinema")) {
                description = elems[1].get(i).child(0).text();
                location = "";
            }
                else {
                    location = getLocation(elems[1], i);
                    description = "";
                }
             events.add(new Event(eventName, eventUrl, description, location, type_of_event));
            if(! type_of_event.equals("cinema")) setTime(events.get(i),elems[2].get(i));
        }

        return events;
    }

}
