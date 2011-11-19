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

        org.hibernate.tutorial.util.HibernateUtil.getSessionFactory().close();
    }

    private Long createAndStoreTask(String name, String command) {
        org.hibernate.Session session = org.hibernate.tutorial.util.HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        fr.prunetwork.task.domain.Task t = new fr.prunetwork.task.domain.Task();
        t.setName(name);
        t.setCommand(command);
        session.save(t);

        session.getTransaction().commit();
        return t.getId();
    }

    private java.util.List listTasks() {
        org.hibernate.Session session = org.hibernate.tutorial.util.HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        java.util.List result = session.createQuery("from Task").list();
        session.getTransaction().commit();
        return result;
    }

    private Long createAndStorePerson(String first, String last) {
        org.hibernate.Session session = org.hibernate.tutorial.util.HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        org.hibernate.tutorial.domain.Person p = new org.hibernate.tutorial.domain.Person();
        session.save(p);

        session.getTransaction().commit();
        return p.getId();
    }

    private void addPersonToEvent(Long personId, Long eventId) {
        org.hibernate.Session session = org.hibernate.tutorial.util.HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        org.hibernate.tutorial.domain.Person aPerson = (org.hibernate.tutorial.domain.Person) session.load(org.hibernate.tutorial.domain.Person.class, personId);
        org.hibernate.tutorial.domain.Event anEvent = (org.hibernate.tutorial.domain.Event) session.load(org.hibernate.tutorial.domain.Event.class, eventId);
        aPerson.addToEvent(anEvent);

        session.getTransaction().commit();
    }

    private void addEmailToPerson(Long personId, String emailAddress) {
        org.hibernate.Session session = org.hibernate.tutorial.util.HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        org.hibernate.tutorial.domain.Person aPerson = (org.hibernate.tutorial.domain.Person) session.load(org.hibernate.tutorial.domain.Person.class, personId);
        // adding to the emailAddress collection might trigger a lazy load of the collection
        aPerson.getEmailAddresses().add(emailAddress);

        session.getTransaction().commit();
    }
}

