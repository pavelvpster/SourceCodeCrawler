/*
 * MainActor.java
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

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Main actor. Creates actor to walk the directory given in message.
 */
public class MainActor extends UntypedActor {

    private final ActorRef indexActor;

    public MainActor(final ActorRef indexActor) {
        this.indexActor = indexActor;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            getContext().actorOf(Props.create(WalkActor.class, indexActor)).forward(message, getContext());
        } else {
            unhandled(message);
        }
    }
}
