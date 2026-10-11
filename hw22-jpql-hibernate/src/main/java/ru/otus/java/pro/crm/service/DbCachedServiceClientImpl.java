package ru.otus.java.pro.crm.service;

import ru.otus.java.pro.core.cache.HwCache;
import ru.otus.java.pro.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbCachedServiceClientImpl implements DBServiceClient {

    private final DBServiceClient dbServiceClient;
    private final HwCache<String, Client> cache;

    public DbCachedServiceClientImpl(
            DBServiceClient dbServiceClient,
            HwCache<String, Client> cache
    ) {
        this.dbServiceClient = dbServiceClient;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        Client savedClient = dbServiceClient.saveClient(client);

        String key = String.valueOf(savedClient.getId());
        cache.put(key, savedClient);

        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        String key = String.valueOf(id);
        Client cachedClient = cache.get(key);

        if (cachedClient != null) {
            return Optional.of(cachedClient);
        }

        Optional<Client> clientOptional = dbServiceClient.getClient(id);
        clientOptional.ifPresent(client -> cache.put(key, client));

        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        return dbServiceClient.findAll();
    }
}