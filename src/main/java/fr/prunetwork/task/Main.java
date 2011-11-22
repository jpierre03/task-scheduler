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

package fr.prunetwork.task;

import fr.prunetwork.task.domain.Task;
import org.hibernate.Session;
import org.hibernate.tutorial.domain.Event;
import org.hibernate.tutorial.domain.Person;
import org.hibernate.tutorial.util.HibernateUtil;

public class Main {

    public static void main(String[] args) {
        Main mgr = new Main();

        for (int i = 0; i < 1000; i++) {
            mgr.createAndStoreTask("My Task", "CLI");
        }

        java.util.List l = mgr.listTasks();
        for (Object o : l) {
            System.out.println(o);
        }

        HibernateUtil.getSessionFactory().close();
    }

    private Long createAndStoreTask(String name, String command) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Task t = new Task();
        t.setName(name);
        t.setCommand(command);
        session.save(t);

        session.getTransaction().commit();
        return t.getId();
    }

    private java.util.List listTasks() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        java.util.List result = session.createQuery("from Task").list();
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

