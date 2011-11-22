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
import org.hibernate.tutorial.util.HibernateUtil;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Main mgr = new Main();
        if (args.length > 0) {

            if ("listTask".equalsIgnoreCase(args[0])) {
                List l = mgr.listTasks();
                for (Object o : l) {
                    System.out.println(o);
                }
            }

            if ("addTask".equalsIgnoreCase(args[0]) && args.length > 3) {
                mgr.createAndStoreTask(args[1], args[2]);
            }
        } else {
            for (int i = 0; i < 10; i++) {
                mgr.createAndStoreTask("My Task", "CLI");
            }

            List l = mgr.listTasks();
            for (Object o : l) {
                System.out.println(o);
            }
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

    private List listTasks() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from Task").list();
        session.getTransaction().commit();
        return result;
    }
}

