package sn.etudiant.l2gl.app;

import sn.etudiant.l2gl.app.model.*;

public class Main {

    public static void main(String[] args) {

        separateur("Q1 — Hiérarchie Personne / Etudiant / Enseignant");
        testQ1();

        separateur("Q2 — Surcharge vs Redéfinition");
        testQ2();

        separateur("Q3 — Interface Affichable + polymorphisme par contrat");
        testQ3();

        separateur("Q4 — Note : invariants et contraintes métier");
        testQ4();

        separateur("Q5 — Identité métier (commentaires dans les classes)");
        testQ5();

        separateur("Q6 — equals / hashCode");
        testQ6();

        separateur("Q7 — Méthode template carte() avec final");
        testQ7();

        separateur("Q8 — Duo<A,B> générique");
        testQ8();

        separateur("Q9 — TableauxUtils.indexOf générique");
        testQ9();

        separateur("Q10 — Registre<T extends Identifiable>");
        testQ10();
    }

    static void testQ1() {

        Etudiant e = new Etudiant("Aminata Diallo", "aminata@univ.sn", "2024-001");
        Enseignant p = new Enseignant("Dr. Sow", "sow@univ.sn", "ENS-042");

        System.out.println("Etudiant    : " + e);
        System.out.println("Enseignant  : " + p);

        try {
            new Etudiant("", "x@y.com", "2024-002");
        } catch (IllegalArgumentException ex) {
            System.out.println("OK - nom vide refusé : " + ex.getMessage());
        }

        try {
            new Etudiant("Fatou", "fatougmail.com", "2024-003");
        } catch (IllegalArgumentException ex) {
            System.out.println("OK - email sans @ refusé : " + ex.getMessage());
        }

        try {
            new Etudiant("Omar", "omar@univ.sn", "ABC");
        } catch (IllegalArgumentException ex) {
            System.out.println("OK - matricule invalide refusé : " + ex.getMessage());
        }
    }

    static void testQ2() {
        Etudiant etudiant = new Etudiant("Aminata Diallo", "aminata@univ.sn", "2024-001");

        Personne p = etudiant;
        System.out.println("p.identite() via Personne → Etudiant.identite() (runtime) :");
        System.out.println("  " + p.identite());

        System.out.println("etudiant.identite(\"PROMO24\") via Etudiant (surcharge compilateur) :");
        System.out.println("  " + etudiant.identite("PROMO24"));


        System.out.println("CONCLUSION : surcharge = compilateur (type déclaré),"
            + " redéfinition = runtime (type réel).");
    }

    static void testQ3() {
        Etudiant e1 = new Etudiant("Aminata Diallo", "aminata@univ.sn", "2024-001");
        Etudiant e2 = new Etudiant("Ibrahima Fall",  "ib@univ.sn",      "2024-002");
        Module   m1 = new Module("INF101", "Algorithmique");
        Module   m2 = new Module("MAT201", "Analyse");

        Affichable[] elements = { e1, e2, m1, m2 };

        System.out.println("Affichage polymorphe via Affichable[] :");
        for (Affichable a : elements) {
            System.out.println("  " + a.afficher());
        }
        System.out.println("CONCLUSION : 'est un' (héritage) vs 'sait faire' (interface).");
    }

    static void testQ4() {
        Etudiant e = new Etudiant("Aminata Diallo", "aminata@univ.sn", "2024-001");
        Module   m = new Module("INF101", "Algorithmique");

        Note n1 = new Note(e, m, 8.0);
        Note n2 = new Note(e, new Module("MAT201", "Analyse"), 13.5);
        Note n3 = new Note(e, new Module("PHY301", "Physique"), 17.0);

        System.out.println(n1 + " | validée=" + n1.estValidee());
        System.out.println(n2 + " | validée=" + n2.estValidee());
        System.out.println(n3 + " | validée=" + n3.estValidee());

        n1.setValeur(11.0);
        System.out.println("Après correction : " + n1);

        try {
            n1.setValeur(21.0);
        } catch (IllegalArgumentException ex) {
            System.out.println("OK - valeur hors [0,20] refusée : " + ex.getMessage());
        }

        try {
            new Note(null, m, 10.0);
        } catch (IllegalArgumentException ex) {
            System.out.println("OK - etudiant null refusé : " + ex.getMessage());
        }
    }

    static void testQ5() {
        Etudiant e = new Etudiant("Aminata", "a@univ.sn", "2024-001");
        Module   m = new Module("INF101", "Algorithmique");

        System.out.println("Etudiant.getMatricule() : " + e.getMatricule());
        System.out.println("Module.getCode()        : " + m.getCode());
        System.out.println("Aucun setter sur matricule ou code — identité garantie immuable.");
    }

    static void testQ6() {
        Etudiant e1 = new Etudiant("Aminata Diallo", "aminata@univ.sn", "2024-001");
        Etudiant e2 = new Etudiant("Aminata Diallo", "aminata@univ.sn", "2024-001"); // même matricule
        Etudiant e3 = new Etudiant("Fatou Ndiaye",   "fatou@univ.sn",  "2024-002");

        System.out.println("e1 == e2    (identité Java) : " + (e1 == e2));          // false
        System.out.println("e1.equals(e2) (même matricule) : " + e1.equals(e2));     // true
        System.out.println("e1.equals(e3) (matricule diff) : " + e1.equals(e3));     // false

        Module m1 = new Module("INF101", "Algorithmique");
        Module m2 = new Module("INF101", "Algo (bis)"); 

        System.out.println("m1.equals(m2) (même code) : " + m1.equals(m2));          

        Note n1 = new Note(e1, m1, 12.0);
        Note n2 = new Note(e2, m2, 18.0); 

        System.out.println("n1.equals(n2) (même identité, valeur diff) : " + n1.equals(n2)); // true

        System.out.println("hashCode e1=" + e1.hashCode() + " hashCode e2=" + e2.hashCode()
            + " → identiques=" + (e1.hashCode() == e2.hashCode()));
    }

    static void testQ7() {
        Personne e = new Etudiant("Aminata Diallo", "aminata@univ.sn", "2024-001");
        Personne p = new Enseignant("Dr. Sow", "sow@univ.sn", "ENS-042");

        System.out.println("--- Carte Etudiant ---");
        System.out.println(e.carte());
        System.out.println("--- Carte Enseignant ---");
        System.out.println(p.carte());
    }

    static void testQ8() {
        Module  m = new Module("INF101", "Algorithmique");
        Etudiant e = new Etudiant("Aminata", "a@univ.sn", "2024-001");

        Duo<Module, Double>  duo1 = new Duo<>(m, 14.5);
        Duo<String, Etudiant> duo2 = new Duo<>("clé-principale", e);

        System.out.println("duo1 (Module, Double)   : " + duo1);
        System.out.println("duo2 (String, Etudiant) : " + duo2);

        Duo<Etudiant, String> duo3 = new Duo<>(e, "clé-secondaire");
        System.out.println("duo2.getA() type : " + duo2.getA().getClass().getSimpleName()); // String
        System.out.println("duo3.getA() type : " + duo3.getA().getClass().getSimpleName()); // Etudiant
        System.out.println("duo2.equals(duo3) : " + duo2.equals(duo3)); // false (ordre différent)

        try {
            new Duo<>(null, "x");
        } catch (IllegalArgumentException ex) {
            System.out.println("OK - null refusé dans Duo : " + ex.getMessage());
        }
    }
    static void testQ9() {
        String[] mots = {"bonjour", "monde", "java", null, "fin"};
        System.out.println("indexOf 'java'   : " + TableauxUtils.indexOf(mots, "java"));   // 2
        System.out.println("indexOf null     : " + TableauxUtils.indexOf(mots, null));      // 3
        System.out.println("indexOf 'absent' : " + TableauxUtils.indexOf(mots, "absent")); // -1

        Etudiant e1 = new Etudiant("Aminata", "a@univ.sn", "2024-001");
        Etudiant e2 = new Etudiant("Fatou",   "f@univ.sn", "2024-002");
        Etudiant eCherche = new Etudiant("Aminata2", "a2@univ.sn", "2024-001"); // même matricule
        Etudiant[] etudiants = {e1, e2};
        System.out.println("indexOf eCherche (même matricule) : " + TableauxUtils.indexOf(etudiants, eCherche)); // 0

        Module m1 = new Module("INF101", "Algorithmique");
        Module m2 = new Module("MAT201", "Analyse");
        Module[] modules = {m1, m2};
        System.out.println("indexOf MAT201 : " + TableauxUtils.indexOf(modules, new Module("MAT201", "X"))); // 1
    }

    static void testQ10() {
        Registre<Etudiant> regEtu = new Registre<>(5);

        Etudiant e1 = new Etudiant("Aminata", "a@univ.sn", "2024-001");
        Etudiant e2 = new Etudiant("Fatou",   "f@univ.sn", "2024-002");

        regEtu.ajouter(e1);
        regEtu.ajouter(e2);
        System.out.println("Taille registre étudiants : " + regEtu.taille()); // 2

        // Recherche
        Etudiant trouve = regEtu.chercherParId("2024-001");
        System.out.println("Trouvé par '2024-001' : " + trouve);
        System.out.println("Introuvable '9999-999' : " + regEtu.chercherParId("9999-999")); // null

        // Doublon : même matricule → exception
        try {
            Etudiant doublon = new Etudiant("Aminata Bis", "abis@univ.sn", "2024-001");
            regEtu.ajouter(doublon);
        } catch (IllegalArgumentException ex) {
            System.out.println("OK - doublon refusé : " + ex.getMessage());
        }

        // Registre de modules
        Registre<Module> regMod = new Registre<>(3);
        regMod.ajouter(new Module("INF101", "Algorithmique"));
        regMod.ajouter(new Module("MAT201", "Analyse"));
        System.out.println("Taille registre modules : " + regMod.taille());
    }

    // -------------------------------------------------------------------------
    // Utilitaire affichage
    // -------------------------------------------------------------------------
    static void separateur(String titre) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + titre);
        System.out.println("=".repeat(60));
    }
}