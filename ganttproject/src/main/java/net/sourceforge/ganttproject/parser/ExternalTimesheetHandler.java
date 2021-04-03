/*
Copyright 2003-2012 Dmitry Barashev, GanttProject Team

This file is part of GanttProject, an opensource project management tool.

GanttProject is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

GanttProject is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with GanttProject.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.ganttproject.parser;

import biz.ganttproject.core.chart.render.ShapePaint;
import biz.ganttproject.core.time.GanttCalendar;
import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.gui.TaskTreeUIFacade;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.task.ResourceAssignment;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.TaskManager.TaskBuilder;
import org.xml.sax.Attributes;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

public class ExternalTimesheetHandler implements TagHandler {
  private final TaskManager myManager;
  private HumanResource humanResource;

  public ExternalTimesheetHandler(TaskManager mgr, HumanResource hr) {
    myManager = mgr;
    humanResource = hr;
  }
  @Override
  public boolean hasCdata() {
    return false;
  }

  @Override
  public void appendCdata(String cdata) {

  }

  @Override
  public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws FileFormatException {
    System.out.println(qName);
    if (qName == "task") {
      loadTask(attrs);
    }
  }

  public void endElement(String namespaceURI, String sName, String qName) {

  }

  private void loadTask(Attributes attrs) {
    TaskBuilder builder = getManager().newTaskBuilder();

    String start = attrs.getValue("start");
    String end = attrs.getValue("end");
    System.out.println(start);
    System.out.println(end);
    if (start != null && end != null) {
      builder = builder.withStartDate(GanttCalendar.parseXMLDate(start).getTime()).withEndDate(GanttCalendar.parseXMLDate(end).getTime());
      Task task = builder.build();
      humanResource.createExternalAssignment(task);
    }

  }

  private TaskManager getManager() {
    return myManager;
  }
}
