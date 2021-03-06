/*******************************************************************************
 * A Task Manager software. Copyright (C) 2011 Jean-Pierre PRUNARET
 * <jean-pierre+taskmgr@spam.prunetwork.fr>  (without spam.)
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License,
 * or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 ******************************************************************************/

package org.hibernate.tutorial;

import org.hibernate.Session;
import org.hibernate.tutorial.domain.Event;
import org.hibernate.tutorial.domain.Person;
import org.hibernate.tutorial.util.HibernateUtil;

import java.util.Date;
import java.util.List;

public class EventManager {

    public static void main(String[] args) {
        EventManager mgr = new EventManager();

        if (args.length > 0) {
            if (args[0].equals("store")) {
                mgr.createAndStoreEvent("My Event", new Date());
            } else if (args[0].equals("list")) {
                List events = mgr.listEvents();
                for (int i = 0; i < events.size(); i++) {
                    Event theEvent = (Event) events.get(i);
                    System.out.println(
                            "Event: " + theEvent.getTitle() + " Time: " + theEvent.getDate()
                    );
                }
            } else if (args[0].equals("addpersontoevent")) {
                Long eventId = mgr.createAndStoreEvent("My Event", new Date());
                Long personId = mgr.createAndStorePerson("Foo", "Bar");
                mgr.addPersonToEvent(personId, eventId);
                System.out.println("Added person " + personId + " to event " + eventId);
            }
        } else {
            for (int i = 0; i < 1000; i++) {
                mgr.createAndStoreEvent("My Event", new Date());
            }

            List l = mgr.listEvents();
            for (Object o : l) {
                System.out.println(o);
            }
        }

        HibernateUtil.getSessionFactory().close();
    }

    private Long createAndStoreEvent(String title, Date theDate) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Event theEvent = new Event();
        theEvent.setTitle(title);
        theEvent.setDate(theDate);
        session.save(theEvent);

        session.getTransaction().commit();
        return theEvent.getId();
    }

    private List listEvents() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from Event").list();
        session.getTransaction().commit();
        return result;
    }

    private Long createAndStorePerson(String first, String last) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person p = new Person();
        session.save(p);

        session.getTransaction().commit();
        return p.getId();
    }

    private void addPersonToEvent(Long personId, Long eventId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person aPerson = (Person) session.load(Person.class, personId);
        Event anEvent = (Event) session.load(Event.class, eventId);
        aPerson.addToEvent(anEvent);

        session.getTransaction().commit();
    }

    private void addEmailToPerson(Long personId, String emailAddress) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person aPerson = (Person) session.load(Person.class, personId);
        // adding to the emailAddress collection might trigger a lazy load of the collection
        aPerson.getEmailAddresses().add(emailAddress);

        session.getTransaction().commit();
    }
}

