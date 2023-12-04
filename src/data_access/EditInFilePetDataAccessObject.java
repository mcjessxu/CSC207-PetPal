package data_access;

import entities.Pet;
import entities.PetFactory;
import use_case.edit.EditPetDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EditInFilePetDataAccessObject implements EditPetDataAccessInterface {

    private final File csvFile;

    private final Map<String, Integer> headers = new LinkedHashMap<>();

    private final Map<String, Pet> pets = new HashMap<>();
    // unique pet name to pet object

    private PetFactory petFactory;

    public EditInFilePetDataAccessObject(String csvPath, PetFactory petFactory) throws IOException {
        this.petFactory = petFactory;

        csvFile = new File(csvPath);
        headers.put("petname", 0);
        headers.put("bio", 1);
        headers.put("owner", 2);

        if (csvFile.length() == 0) {
            save();
        } else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                String header = reader.readLine();

                // For later: clean this up by creating a new Exception subclass and handling it in the UI.
                assert header.equals("petname,bio,owner");

                String row;
                while ((row = reader.readLine()) != null) {
                    String[] col = row.split(",");
                    String petname = String.valueOf(col[headers.get("petname")]);
                    String bio = String.valueOf(col[headers.get("bio")]);
                    String owner = String.valueOf(col[headers.get("owner")]);

                    Pet pet = petFactory.create(null, null, null, petname, null, null,
                            null, null, null, null, null, null, false,
                            null, null, null, bio, owner);
                    pets.put(petname, pet);
                }
            }
        }
    }

    @Override
    public void save(Pet pet) {
        pets.put(pet.getName(), pet);
        this.save();
    }

    private void save() {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (Pet pet : pets.values()) {
                String line = String.format("%s,%s,%s",
                        pet.getName(), pet.getBio(), pet.getOwner());
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean existsByName(String identifier) {
        return pets.containsKey(identifier);
    }

}
