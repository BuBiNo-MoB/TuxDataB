package com.database.TuxDataB.linuxDistribution;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinuxDistributionService {

    private final LinuxDistributionRepository repository;
    private final ModelMapper modelMapper;

    public LinuxDistributionDTO create(LinuxDistributionDTO distributionDTO) {
        LinuxDistribution distribution = modelMapper.map(distributionDTO, LinuxDistribution.class);
        return modelMapper.map(repository.save(distribution), LinuxDistributionDTO.class);
    }

    public LinuxDistributionDTO update(Long id, LinuxDistributionDTO distributionDTO) {
        LinuxDistribution distribution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distribution not found"));
        modelMapper.map(distributionDTO, distribution);
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

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
