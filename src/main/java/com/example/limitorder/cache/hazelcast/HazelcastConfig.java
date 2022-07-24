package com.example.limitorder.cache.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.impl.HazelcastInstanceImpl;
import com.hazelcast.memory.MemorySize;
import com.hazelcast.memory.MemoryUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class HazelcastConfig {

    @Configuration
    @ConditionalOnMissingBean(ClientConfig.class)
    static class HazelcastClientConfigConfiguration {

        @Bean
        public Config config() throws Exception {
            log.warn("Creating 'ClientConfig' manually not autoconfigure");
            Config config = new Config();
            config.getNetworkConfig()
                    .setPort( 5900 )
                    .setPortAutoIncrement( false );
            //https://docs.hazelcast.com/hazelcast/5.1/serialization/implementing-portable-serialization
            SerializationConfig srzConfig = config.getSerializationConfig();
            srzConfig.addPortableFactory( 1, new OrderPortableFactory())
                    .setAllowUnsafe(true)
                    .setEnableCompression(true)
                    .setUseNativeByteOrder(true);

            config.setNativeMemoryConfig(getNativeMemoryConfig());
            return config;
        }
    }

    private static NativeMemoryConfig getNativeMemoryConfig() {
        //https://docs.hazelcast.com/hazelcast/5.1/storage/high-density-memory
        MemorySize memorySize = new MemorySize(512, MemoryUnit.MEGABYTES);
        NativeMemoryConfig nativeMemoryConfig =
                new NativeMemoryConfig()
                        .setAllocatorType(NativeMemoryConfig.MemoryAllocatorType.POOLED)
                        .setSize(memorySize)
                        .setEnabled(true)
                        .setMinBlockSize(16)
                        .setPageSize(1 << 20);
        return nativeMemoryConfig;
    }

    @Configuration
    @ConditionalOnMissingBean(HazelcastInstance.class)
    static class HazelcastClientConfiguration {

        @Bean
        public HazelcastInstance hazelcastInstance(Config config) {
            log.warn("Creating client 'HazelcastInstance' manually not autoconfigure");
            return Hazelcast.newHazelcastInstance(config);
        }
    }
}

