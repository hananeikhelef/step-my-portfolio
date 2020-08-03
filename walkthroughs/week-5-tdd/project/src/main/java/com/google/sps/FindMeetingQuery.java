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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;	
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.*;
import java.util.HashSet;

import java.util.Collection;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

        Collection<TimeRange> timeSlots = new ArrayList();
        int startTime= TimeRange.getTimeInMinutes(0, 0);
        int endTime = TimeRange.getTimeInMinutes(0, 0);

        // check for invalid duration
        if((request.getDuration() > TimeRange.WHOLE_DAY.duration()) || request.getAllAttendees().isEmpty() ) {
            return timeSlots;
        }

        List<TimeRange> eventTimeRanges = getEventTimeRanges(events,request.getAllAttendees());
        Collection<TimeRange> meetingRanges = getMeetingRanges(eventTimeRanges,request.getDuration());

       // whenever no schedulued events exists, we can schedule in any slot  
       // If there are no time ranges that work for all attendees, get the time
       // ranges that work for only the mandatory attendees.
       // In the case that there are optional attendees but no mandatory attendees, 
       // treat the optional attendees as the mandatory attendees
       
        if (meetingRanges.isEmpty()) {
            List<TimeRange> blockedTimeRanges = 
                getEventTimeRanges(events, request.getAttendees());
            Collection<TimeRange> meetingTime = 
                getMeetingRanges(blockedTimeRanges, request.getDuration());
            return meetingTime;
        }
        else {
            return meetingRanges;
        }
    }

  // The algorithm adds timeranges as we go through the events starting from the start day
  public List<TimeRange> getEventTimeRanges(Collection<Event> events, Collection<String> request){
        // sort all the time ranges and add  to the list the required attendees in the meeting request
        List<TimeRange> eventTimeRanges = 
        events.stream()
            .filter(event -> event.getAttendees().stream()
                .anyMatch(attendee -> request.contains(attendee)))
            .map(i -> i.getWhen())
            .collect(Collectors.toList());
        Collections.sort(eventTimeRanges, TimeRange.ORDER_BY_START);
        return eventTimeRanges;
    } 
    
  /**
   * Returns a copy of all the people requested this meeting.
   * Function that returns all valid time ranges based on a list of blocked time ranges
   * Approach: start from start of day, and add available timeranges as we traverse through the events' timerange
   */
  public Collection<TimeRange> getMeetingRanges(List<TimeRange> eventTimeRange, long duration){

        Collection<TimeRange> timeSlots = new ArrayList();
        int startTime = TimeRange.getTimeInMinutes(0, 0);
        int endTime = TimeRange.getTimeInMinutes(0, 0);

        for (TimeRange event: eventTimeRange) {
            endTime = event.start();
            if (endTime-startTime >= duration) {
                timeSlots.add(TimeRange.fromStartEnd(startTime,endTime,false));
            }
            if (event.end() > startTime) {
                startTime = event.end();
            }
        }
        // Add the last event if applicable.
        if (TimeRange.END_OF_DAY - startTime >= duration){
            timeSlots.add(TimeRange.fromStartEnd(startTime,TimeRange.END_OF_DAY,true));
        }
        return timeSlots;
    }
}
