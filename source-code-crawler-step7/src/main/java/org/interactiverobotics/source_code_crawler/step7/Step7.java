/*
 * Step7.java
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
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.routing.RoundRobinPool;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Step7.
 */
public class Step7 {

    private static final String PATH = "source-code-crawler-step1";

    public static void main(String[] args) throws Exception {
        final ActorSystem actorSystem = ActorSystem.create();
        final ActorRef indexActor = actorSystem.actorOf(Props.create(IndexActor.class)
                .withRouter(new RoundRobinPool(4)));
        final ActorRef mainActor = actorSystem.actorOf(Props.create(MainActor.class, indexActor));
        printIndex((Map<String, List<String>>) Await.result(
                Patterns.ask(mainActor, PATH, new Timeout(1, TimeUnit.HOURS)), Duration.Inf()));
        Await.ready(actorSystem.terminate(), Duration.Inf());
    }
}
