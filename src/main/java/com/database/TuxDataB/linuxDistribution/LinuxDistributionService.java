package com.database.TuxDataB.linuxDistribution;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinuxDistributionService {

    private final LinuxDistributionRepository repository;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;

    public LinuxDistributionDTO create(LinuxDistributionDTO distributionDTO) {
        LinuxDistribution distribution = modelMapper.map(distributionDTO, LinuxDistribution.class);
        return modelMapper.map(repository.save(distribution), LinuxDistributionDTO.class);
    }

    public LinuxDistributionDTO update(Long id, LinuxDistributionDTO distributionDTO) {
        LinuxDistribution distribution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distribution not found"));
        modelMapper.map(distributionDTO, distribution);
        distribution.setId(id);

        return modelMapper.map(repository.save(distribution), LinuxDistributionDTO.class);
    }

    public LinuxDistributionDTO findById(Long id) {
        return repository.findById(id)
                .map(distribution -> modelMapper.map(distribution, LinuxDistributionDTO.class))
                .orElseThrow(() -> new RuntimeException("Distribution not found"));
    }

    public List<LinuxDistributionDTO> findAll() {
        return repository.findAll().stream()
                .map(distribution -> modelMapper.map(distribution, LinuxDistributionDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<LinuxDistributionDTO> searchByName(String keyword) {
        return repository.searchByNameContaining(keyword).stream()
                .map(distribution -> modelMapper.map(distribution, LinuxDistributionDTO.class))
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<LinuxDistributionDTO> findByDesktopEnvironment(String desktopEnvironment) {
        return repository.findByDesktopEnvironment(desktopEnvironment).stream()
                .map(distribution -> modelMapper.map(distribution, LinuxDistributionDTO.class))
                .collect(Collectors.toList());
    }

    public String uploadLogo(Long id, MultipartFile logo) throws IOException {
        LinuxDistribution distribution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distribution not found"));

        Map<String, Object> uploadResult = cloudinary.uploader().upload(logo.getBytes(), ObjectUtils.emptyMap());
        String url = (String) uploadResult.get("url");

        distribution.setLogoUrl(url);
        repository.save(distribution);

        return url;
    }

    public String uploadDesktopImage(Long id, MultipartFile desktopImage) throws IOException {
        LinuxDistribution distribution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distribution not found"));

        Map<String, Object> uploadResult = cloudinary.uploader().upload(desktopImage.getBytes(), ObjectUtils.emptyMap());
        String url = (String) uploadResult.get("url");

        distribution.setDesktopImageUrl(url);
        repository.save(distribution);

        return url;
    }

    public String updateLogo(Long id, MultipartFile logo) throws IOException {
        deleteLogo(id);
        return uploadLogo(id, logo);
    }

    public String updateDesktopImage(Long id, MultipartFile desktopImage) throws IOException {
        deleteDesktopImage(id);
        return uploadDesktopImage(id, desktopImage);
    }

    public void deleteLogo(Long id) throws IOException {
        LinuxDistribution distribution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distribution not found"));

        if (distribution.getLogoUrl() != null) {
            cloudinary.uploader().destroy(distribution.getLogoUrl(), ObjectUtils.emptyMap());
            distribution.setLogoUrl(null);
            repository.save(distribution);
        }
    }

    public void deleteDesktopImage(Long id) throws IOException {
        LinuxDistribution distribution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distribution not found"));

        if (distribution.getDesktopImageUrl() != null) {
            cloudinary.uploader().destroy(distribution.getDesktopImageUrl(), ObjectUtils.emptyMap());
            distribution.setDesktopImageUrl(null);
            repository.save(distribution);
        }
    }
}
