package dev.hero.projectmanager.util;

import dev.hero.projectmanager.repository.OrganizationRepository;

public class SlugUtils
{
    public static String generateSlug(String name)
    {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    public static String generateUniqueSlug(String name, OrganizationRepository repo)
    {
        String base = generateSlug(name);
        String slug = base;

        int counter = 1;

        while (repo.existsBySlug(slug)) {
            slug = base + "-" + counter++;
        }

        return slug;
    }
}
