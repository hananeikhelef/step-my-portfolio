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
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

      Collection<TimeRange> timeSlots = new ArrayList();
        long duration = request.getDuration();
        int start = TimeRange.getTimeInMinutes(0, 0);
        int end = TimeRange.getTimeInMinutes(0, 0);
        int lastEnd = TimeRange.getTimeInMinutes(0, 0);

         List<TimeRange> eventTimeRanges = events.stream().filter(event -> event.getAttendees().stream().anyMatch(attendee -> request.getAttendees().contains(attendee)))
                .map(e -> e.getWhen())
                .collect(Collectors.toList());
                
        Collections.sort(eventTimeRanges, TimeRange.ORDER_BY_START);
         if(request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
            return timeSlots;
        }
        if (eventTimeRanges.size() == 0){
            timeSlots.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY,TimeRange.END_OF_DAY,true));
        }
        else {
            for (TimeRange event: eventTimeRanges) {
                end = event.start();
                if (end-start >= duration) {
                    timeSlots.add(TimeRange.fromStartEnd(start,end,false));
                }
                start = event.end();
                 if (event.end() > lastEnd) {
                    lastEnd = event.end();
                }
            }
            // Add the last event if applicable.
            if (TimeRange.END_OF_DAY - lastEnd >= duration){
                timeSlots.add(TimeRange.fromStartEnd(lastEnd,TimeRange.END_OF_DAY,true));
            }
        }
        return timeSlots;
  }
  
}
