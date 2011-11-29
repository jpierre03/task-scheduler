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
import fr.prunetwork.task.visitor.TaskGraphvizVisitor;
import fr.prunetwork.task.visitor.TaskHibernateHandlerVisitor;
import org.hibernate.Session;
import org.hibernate.tutorial.util.HibernateUtil;

import java.io.PrintStream;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Main mgr = new Main();
        if (args.length > 0) {

            if ("--list-Task".equalsIgnoreCase(args[0])) {
                mgr.displayTasks(System.out);
            }

            if ("--flush-Task".equalsIgnoreCase(args[0])) {
                mgr.flushTasks();
            }

            if ("--add-Task".equalsIgnoreCase(args[0])) {
//                mgr.createAndStoreTask("MyTask", "CLI");
                mgr.createAndStoreTask_massive("MyTask", "CLI", 1 * 1000);

                mgr.displayTasks(System.out);
            }

            if ("--sample-Task".equalsIgnoreCase(args[0])) {
                Task sap = Task.createTask("sap-EC2", "true");
                Task sap_auto = Task.createTask("sap-control-m", "true");
                Task zsd021 = Task.createTask("zsd021", "\\\\pica\\zsd021.exe");

                sap_auto.addParent(sap);
                zsd021.addParent(sap_auto);


                zsd021.accept(new TaskHibernateHandlerVisitor());
//                mgr.storeTask(sap);
//                mgr.storeTask(sap_auto);
//                mgr.storeTask(zsd021);

//                mgr.displayTasks(System.out);
//                zsd021.accept(new TaskPrintVisitor());

                System.out.println("digraph task{");
                zsd021.accept(new TaskGraphvizVisitor(System.out));
                System.out.println("}");

            }

            if ("--hammer".equalsIgnoreCase(args[0])) {
                mgr.createAndStoreTask_massive("MyTask", "CLI", 10 * 1000 * 1000);
            }

        } else {
            // Automatically add and display
            mgr.createAndStoreTask_massive("MyTask", "CLI", 10);

            mgr.displayTasks(System.out);
        }

        HibernateUtil.getSessionFactory().close();

//        assert (false) : "This should fail";
//        throw new Error("Add vm parameter -ea (or -enableassertions) to enable assertion checks");
    }

    private void storeTask(Task task) {
        assert (task != null);
    }

    private Task createAndStoreTask(String name, String command) {
        assert (name != null);
        assert (name != "");
        assert (command != null);
        assert (command != "");

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Task t = Task.createTask(name, command);

        assert (t != null);
        session.save(t);

        session.getTransaction().commit();
        return t;
    }

    private void createAndStoreTask_massive(String name, String command, int count) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();


        Task t1 = Task.createTask(name, command);
        session.save(t1);

        for (long i = 0; i < count - 1; i++) {
            Task t = Task.createTask(name, command);

            t.addParent(t1);

            session.save(t);
        }

        session.getTransaction().commit();
    }

    private List listTasks() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from Task").list();
        session.getTransaction().commit();
        return result;
    }

    private void displayTasks(PrintStream out) {
        List l = this.listTasks();
        for (Object o : l) {
            out.println(o);
        }
    }

    private void flushTasks() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        for (Object o : session.createQuery("from Task").list()) {
            session.delete(o);
        }
        session.getTransaction().commit();
    }
}

