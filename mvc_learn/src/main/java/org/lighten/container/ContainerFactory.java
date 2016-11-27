package org.lighten.container;

import org.lighten.Config;

import javax.security.auth.DestroyFailedException;
import java.util.List;

/**
 * Factory instance for creating IoC container.
 *
 * Created by yefeng on 16/11/27.
 */
public interface ContainerFactory {

    /**
     * Init container factory.
     *
     * @param config
     */
    void init(Config config);

    /**
     * Find all beans in container.
     *
     * @return
     */
    List<Object> findAllBeans();

    /**
     * When container destroyed.
     */
    void destroy() throws DestroyFailedException;
}
