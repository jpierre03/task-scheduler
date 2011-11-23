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

package fr.prunetwork.task.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jean-Pierre PRUNARET
 *         Date: 19/11/11 22:54
 */
@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String command;

    @ManyToMany
    private Set<Task> parents = new HashSet<Task>();

    public static Task createTask(String name, String command) {
        assert (name != null);
        assert (command != null);

        Task t = new Task();
        t.name = name;
        t.command = command;

        return t;
    }

    private Task() {
    }

    private Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Transient
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(id).append("\";");
        sb.append("\"").append(name).append("\";");
        sb.append("\"").append(command).append("\";");
//        sb.append("\"").append(parents.toArray()).append("\"");
        return sb.toString();
    }

    public void addParent(Task parent) {
        assert (parent != null);
        this.getParents().add(parent);
    }


    private Set<Task> getParents() {
        return parents;
    }

    private void setParents(Set<Task> parents) {
        this.parents = parents;
    }


}

