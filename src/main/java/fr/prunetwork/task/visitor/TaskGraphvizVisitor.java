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

package fr.prunetwork.task.visitor;

import fr.prunetwork.task.domain.Task;

import java.io.PrintStream;

/**
 * @author Jean-Pierre PRUNARET
 *         Date: 23/11/11 20:11
 */
public class TaskGraphvizVisitor extends TaskVisitor {
    final PrintStream out;

    public TaskGraphvizVisitor(PrintStream out) {
        assert (out != null);
        this.out = out;
    }

    @Override
    public void visit(Task task) {

        recursiveVisit(task);
        task.toGraphvizLabel(out);
        task.toGraphvizRelation(out);
    }
}
