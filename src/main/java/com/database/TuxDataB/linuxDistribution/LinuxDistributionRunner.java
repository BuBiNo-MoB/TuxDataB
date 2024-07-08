package com.database.TuxDataB.linuxDistribution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Order(1)
public class LinuxDistributionRunner implements ApplicationRunner {

    @Autowired
    private LinuxDistributionRepository distributionRepository;

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (distributionRepository.count() == 0) {
            List<LinuxDistribution> distributions = Arrays.asList(
                    new LinuxDistribution("Ubuntu", "A popular Linux distribution based on Debian.", parseDate("2023-04-20"), "https://ubuntu.com", "Debian", "x86_64, ARM", "deb", "GNOME", "https://example.com/ubuntu-logo.png", "https://example.com/ubuntu-desktop.png", "23.04"),
                    new LinuxDistribution("Fedora", "A cutting-edge Linux distribution sponsored by Red Hat.", parseDate("2023-04-18"), "https://getfedora.org", "Independent", "x86_64, ARM", "rpm", "GNOME", "https://example.com/fedora-logo.png", "https://example.com/fedora-desktop.png", "38"),
                    new LinuxDistribution("Debian", "A versatile and stable Linux distribution.", parseDate("2023-01-10"), "https://debian.org", "Independent", "x86_64, ARM", "deb", "GNOME, KDE, XFCE", "https://example.com/debian-logo.png", "https://example.com/debian-desktop.png", "12"),
                    new LinuxDistribution("Arch Linux", "A lightweight and flexible Linux distribution.", parseDate("2023-05-02"), "https://archlinux.org", "Independent", "x86_64", "tar.zst", "None (user chooses)", "https://example.com/arch-linux-logo.png", "https://example.com/arch-linux-desktop.png", "2023.05.02"),
                    new LinuxDistribution("OpenSUSE", "A stable and easy-to-use Linux distribution.", parseDate("2023-06-01"), "https://opensuse.org", "Independent", "x86_64, ARM", "rpm", "KDE, GNOME", "https://example.com/opensuse-logo.png", "https://example.com/opensuse-desktop.png", "15.4"),
                    new LinuxDistribution("Linux Mint", "A user-friendly Linux distribution based on Ubuntu.", parseDate("2023-04-05"), "https://linuxmint.com", "Ubuntu", "x86_64", "deb", "Cinnamon, MATE, XFCE", "https://example.com/linux-mint-logo.png", "https://example.com/linux-mint-desktop.png", "21.1"),
                    new LinuxDistribution("Manjaro", "A user-friendly Linux distribution based on Arch Linux.", parseDate("2023-03-15"), "https://manjaro.org", "Arch Linux", "x86_64", "tar.zst", "XFCE, KDE, GNOME", "https://example.com/manjaro-logo.png", "https://example.com/manjaro-desktop.png", "22.1.0"),
                    new LinuxDistribution("CentOS", "A community-driven free software effort focused on delivering a robust open source ecosystem.", parseDate("2023-02-03"), "https://centos.org", "Red Hat", "x86_64", "rpm", "GNOME", "https://example.com/centos-logo.png", "https://example.com/centos-desktop.png", "9"),
                    new LinuxDistribution("Kali Linux", "A Debian-based Linux distribution designed for digital forensics and penetration testing.", parseDate("2023-06-10"), "https://kali.org", "Debian", "x86_64, ARM", "deb", "XFCE", "https://example.com/kali-linux-logo.png", "https://example.com/kali-linux-desktop.png", "2023.2"),
                    new LinuxDistribution("Zorin OS", "A powerful, secure, and user-friendly Linux distribution based on Ubuntu.", parseDate("2023-05-23"), "https://zorin.com", "Ubuntu", "x86_64", "deb", "GNOME", "https://example.com/zorin-os-logo.png", "https://example.com/zorin-os-desktop.png", "16.2")
            );

            distributions.forEach(distributionRepository::save);
            System.out.println("--- Distributions added ---");
        }
    }
}
