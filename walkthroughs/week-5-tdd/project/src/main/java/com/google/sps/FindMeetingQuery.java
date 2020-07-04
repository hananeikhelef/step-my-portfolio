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
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;	
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.*;

import java.util.Collection;

public final class FindMeetingQuery {

    // The algorithm adds timeranges as we go through the events starting from the start day
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

        Collection<TimeRange> timeSlots = new ArrayList();
        int startTime= TimeRange.getTimeInMinutes(0, 0);
        int endTime = TimeRange.getTimeInMinutes(0, 0);
        int timeTrack = TimeRange.getTimeInMinutes(0, 0);

        // check for invalid duration
        if(request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
            return timeSlots;
        }

        // sort all the time ranges and add  to the list the required attendees in the meeting request
        List<TimeRange> eventTimeRanges = 
                events.stream().filter(event -> event.getAttendees().stream()
                .anyMatch(attendee -> request.getAttendees().contains(attendee)))
                .map(i -> i.getWhen())
                .collect(Collectors.toList());
                
        Collections.sort(eventTimeRanges, TimeRange.ORDER_BY_START);

       // whenever no schedulued events exists, we can schedule in any slot  
        if (eventTimeRanges.size() == 0){
            timeSlots.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY,TimeRange.END_OF_DAY,true));
            return timeSlots;
        }
        else {
            for (TimeRange event: eventTimeRanges) {
                endTime = event.start();
                if (endTime-startTime >= request.getDuration()) {
                    timeSlots.add(TimeRange.fromStartEnd(startTime,endTime,false));
                }
                startTime = event.end();
                if (event.end() > timeTrack) {
                    timeTrack = event.end();
                }
            }
            // Add the last event if applicable.
            if (TimeRange.END_OF_DAY - timeTrack >= request.getDuration()){
                timeSlots.add(TimeRange.fromStartEnd(timeTrack,TimeRange.END_OF_DAY,true));
            }
        }
        return timeSlots;
  }
  
}
