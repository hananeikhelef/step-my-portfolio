// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;
import com.google.sps.TimeRange;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;

import java.util.Collection;

public final class FindMeetingQuery {

  /**
   * A comparator for sorting ranges by their start time in ascending order.
   */
  public class CompareEvents implements  Comparator<Event> {
      @Override
      public int compare(Event x, Event y){
          return TimeRange.ORDER_BY_START.compare(x.getWhen(), y.getWhen());
      }
  }

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
      ArrayList<TimeRange> timeSlots = new ArrayList<>();
      
      //  get time in minutes 
      int timeInMinutes = TimeRange.START_OF_DAY;
      
      ArrayList<Event>  allEvents = new ArrayList<>(events);
      // sort the event list
      Collections.sort(allEvents,  new CompareEvents());

      for(Event event: allEvents) {
          //  a list of all attendees
         HashSet<String> attendees = new HashSet<>(event.getAttendees());
         // remove all the HashSet's elements that are not contained in the specified collection 
         attendees.retainAll(request.getAttendees());

         if(attendees.size() < 1 ) continue; 

        // get event timing and compare it with the duraiton 
         if(event.getWhen().start() > TimeRange.START_OF_DAY){
              if(((int)request.getDuration() + timeInMinutes) <= event.getWhen().start()){
                  // if the meeting time and the event are long enough add them to the time slots
                 timeSlots.add( event.getWhen().fromStartEnd(timeInMinutes,  event.getWhen().start(), false));
             }
         }
         //  choose the longest timing between the start and the end of the day
        timeInMinutes = Math.max(event.getWhen().end(),timeInMinutes);
      }
       if((int)request.getDuration() + timeInMinutes <= TimeRange.END_OF_DAY){
           timeSlots.add(TimeRange.fromStartEnd(timeInMinutes, TimeRange.END_OF_DAY,true));
       }
       return timeSlots;
  }
}
