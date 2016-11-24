/*
 * WalkActor.java
 *
 * Copyright (C) 2016 Pavel Prokhorov (pavelvpster@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.interactiverobotics.source_code_crawler.step7;

import static org.interactiverobotics.source_code_crawler.common.SourceCodeCrawlerCommon.*;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Walk actor. Walks directory given in message and pass each file to index actor(s).
 */
public class WalkActor extends UntypedActor {

    private final ActorRef indexActor;

    private Map<String, List<String>> index = new HashMap<>();
    private long count = 0;
    private ActorRef client;

    public WalkActor(final ActorRef indexActor) {
        this.indexActor = indexActor;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            client = getSender();
            walk(Paths.get((String) message), file -> {
                count++;
                indexActor.tell(file.toString(), getSelf());
            });
            return;
        }
        if (message instanceof Map) {
            ((Map<String, List<String>>) message).entrySet()
                    .forEach(e -> e.getValue().forEach(v -> addToIndex(e.getKey(), v, index)));
            count--;
            if (count == 0) {
                client.tell(index, getSelf());
                getContext().stop(getSelf());
            }
            return;
        }
        unhandled(message);
    }
}
