package br.com.trustsystems.elfinder.support.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.command.CommandFactory;
import br.com.trustsystems.elfinder.configuration.ElfinderConfigurationWrapper;
import br.com.trustsystems.elfinder.configuration.jaxb.ElfinderConfiguration;
import br.com.trustsystems.elfinder.core.Volume;
import br.com.trustsystems.elfinder.core.VolumeSecurity;
import br.com.trustsystems.elfinder.core.impl.DefaultVolumeSecurity;
import br.com.trustsystems.elfinder.core.impl.SecurityConstraint;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.ElfinderStorageFactory;
import br.com.trustsystems.elfinder.service.VolumeSources;
import br.com.trustsystems.elfinder.service.impl.DefaultElfinderStorage;
import br.com.trustsystems.elfinder.service.impl.DefaultElfinderStorageFactory;
import br.com.trustsystems.elfinder.service.impl.DefaultThumbnailWidth;
import br.com.trustsystems.elfinder.support.locale.LocaleUtils;

@Configuration
@ComponentScan(
        basePackages = {"br.com.trustsystems.elfinder"},
        excludeFilters = {@Filter(type = FilterType.ANNOTATION, value = Configuration.class)}
)
public class ElfinderRootConfig {

    @Bean(name = "commandFactory")
    public CommandFactory getCommandFactory() {
        CommandFactory commandFactory = new CommandFactory();
        commandFactory.setClassNamePattern("br.com.trustsystems.elfinder.command.%sCommand");
        return commandFactory;
    }

    @Bean(name = "elfinderConfiguration")
    public ElfinderConfigurationWrapper getElfinderConfigurationWrapper() {
        return new ElfinderConfigurationWrapper();
    }

    @Bean(name = "elfinderStorageFactory")
    public ElfinderStorageFactory getElfinderStorageFactory() {
        DefaultElfinderStorageFactory elfinderStorageFactory = new DefaultElfinderStorageFactory();
        elfinderStorageFactory.setElfinderStorage(getElfinderStorage());
        return elfinderStorageFactory;
    }

    @Bean(name = "elfinderStorage")
    public ElfinderStorage getElfinderStorage() {
        ElfinderConfigurationWrapper elfinderConfiguration = getElfinderConfigurationWrapper();
        DefaultElfinderStorage defaultElfinderStorage = new DefaultElfinderStorage();

        // creates thumbnail
        DefaultThumbnailWidth defaultThumbnailWidth = new DefaultThumbnailWidth();
        defaultThumbnailWidth.setThumbnailWidth(elfinderConfiguration.getThumbnailWidth());

        // creates volumes, volumeIds, volumeLocale and volumeSecurities
        Character defaultVolumeId = 'A';
        List<ElfinderConfiguration.Volume> elfinderConfigurationVolumes = elfinderConfiguration.getVolumes();
        List<Volume> elfinderVolumes = new ArrayList<>(elfinderConfigurationVolumes.size());
        Map<Volume, String> elfinderVolumeIds = new HashMap<>(elfinderConfigurationVolumes.size());
        Map<Volume, Locale> elfinderVolumeLocales = new HashMap<>(elfinderConfigurationVolumes.size());
        List<VolumeSecurity> elfinderVolumeSecurities = new ArrayList<>();

        // creates volumes
        for (ElfinderConfiguration.Volume elfinderConfigurationVolume : elfinderConfigurationVolumes) {

            final String alias = elfinderConfigurationVolume.getAlias();
            final String path = elfinderConfigurationVolume.getPath();
            final String source = elfinderConfigurationVolume.getSource();
            final String locale = elfinderConfigurationVolume.getLocale();
            final boolean isLocked = elfinderConfigurationVolume.getConstraint().isLocked();
            final boolean isReadable = elfinderConfigurationVolume.getConstraint().isReadable();
            final boolean isWritable = elfinderConfigurationVolume.getConstraint().isWritable();

            // creates new volume
            Volume volume = VolumeSources.of(source).newInstance(alias, path);

            elfinderVolumes.add(volume);
            elfinderVolumeIds.put(volume, Character.toString(defaultVolumeId));
            elfinderVolumeLocales.put(volume, LocaleUtils.toLocale(locale));

            // creates security constraint
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setLocked(isLocked);
            securityConstraint.setReadable(isReadable);
            securityConstraint.setWritable(isWritable);

            // creates volume security
            DefaultVolumeSecurity defaultVolumeSecurity = new DefaultVolumeSecurity();
            defaultVolumeSecurity.setVolumePattern(Character.toString(defaultVolumeId) + ElFinderConstants.ELFINDER_VOLUME_SERCURITY_REGEX);
            defaultVolumeSecurity.setSecurityConstraint(securityConstraint);

            elfinderVolumeSecurities.add(defaultVolumeSecurity);

            // prepare next volumeId character
            defaultVolumeId++;
        }

        defaultElfinderStorage.setThumbnailWidth(defaultThumbnailWidth);
        defaultElfinderStorage.setVolumes(elfinderVolumes);
        defaultElfinderStorage.setVolumeIds(elfinderVolumeIds);
        defaultElfinderStorage.setVolumeLocales(elfinderVolumeLocales);
        defaultElfinderStorage.setVolumeSecurities(elfinderVolumeSecurities);

        return defaultElfinderStorage;
    }

}
