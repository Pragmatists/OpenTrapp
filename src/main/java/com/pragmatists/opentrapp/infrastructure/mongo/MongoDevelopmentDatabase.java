package com.pragmatists.opentrapp.infrastructure.mongo;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
@Profile("mongo-dev")
public class MongoDevelopmentDatabase {

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;

    @PostConstruct
    public static void startDb() throws IOException {
        IMongodConfig mongosConfig = new MongodConfigBuilder()
                .version(Version.V3_0_5)
                .net(new Net(27014, Network.localhostIsIPv6()))
                .build();
        MongodStarter runtime = MongodStarter.getDefaultInstance();
        mongodExe = runtime.prepare(mongosConfig);
        mongod = mongodExe.start();
        System.err.println("----------------- Development database started!");
    }
    
    @PreDestroy
    public static void stopDb() throws Exception {
        mongod.stop();
        mongodExe.stop();
        System.err.println("----------------- Development database stopped!");
    }
}