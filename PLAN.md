# 📱 PocketSheet — Piano di Sviluppo & Architettura Tecnica

Documento di pianificazione strategica per la progettazione e lo sviluppo dell'applicazione mobile Android **PocketSheet**, una scheda personaggio portatile per D&D 5e con supporto per importazione/esportazione JSON, gestione delle statistiche e personalizzazione grafica.

---

## 1. Visione del Progetto & Requisiti

### 1.1 Obiettivo
Creare un'applicazione Android intuitiva, leggera e reattiva che consenta ai giocatori di Dungeons & Dragons 5e di portare sempre con sé i propri personaggi. L'app deve permettere di:
1. **Importare** schede personaggio da file JSON (secondo lo standard evidenziato in `Elandor_Dawnwhisper_5e.json`).
2. **Visualizzare** in modo chiaro le informazioni divise per macro-aree logiche.
3. **Modificare** in tempo reale i valori (punti ferita, slot incantesimi, risorse, equipaggiamento, monete, note).
4. **Personalizzare** il personaggio associando ritratti/immagini personalizzate (come mostrato nei prototipi).
5. **Esportare** la scheda aggiornata in formato JSON per interoperabilità o backup.

---

## 2. Tecnologie e Linguaggi Consigliati

### 2.1 Scelta Primaria Consigliata: **Native Android con Kotlin & Jetpack Compose**

Per un'applicazione Android moderna con requisiti di manipolazione file locali (Storage Access Framework), gestione delle immagini da galleria, interfaccia reattiva e alte performance, lo stack ideale è:

| Componente | Tecnologia Consigliata | Motivazione |
| :--- | :--- | :--- |
| **Linguaggio** | **Kotlin** (v1.9+ / 2.0+) | Linguaggio ufficiale e primario per Android. Sicurezza contro i null (`null safety`), estensioni, coroutine per operazioni asincrone e DSL nativo. |
| **UI Toolkit** | **Jetpack Compose** + **Material 3** | Il framework dichiarativo moderno per Android. Permette di replicare fedelmente i prototipi grafici (card personalizzate, header colorati, griglie di navigazione, bottoni interattivi) con pochissimo codice boilerplate e supporto immediato a Dark/Light Theme. |
| **Architettura** | **Clean Architecture + MVVM** | Modello standard raccomandato da Google (`UI Layer` con Composable & ViewModel, `Domain Layer` con Use Cases, `Data Layer` con Repository). Separa nettamente la logica di gioco dal rendering UI. |
| **Database Locale** | **Room Database** (SQLite) | Consente di memorizzare la lista dei personaggi, le statistiche modificate e i metadati (es. percorso locale dell'immagine) in modo persistente e sicuro, esponendo flussi reattivi (`Flow`). |
| **Serializzazione JSON** | **Kotlinx Serialization** | Libreria Kotlin-first, sicura a tempo di compilazione, estremamente veloce. Gestisce con facilità i tipi misti del JSON (stringhe/interi per HP, dadi, slot) tramite serializer personalizzati. |
| **Caricamento Immagini** | **Coil** (Compose) | Libreria moderna e leggera per Jetpack Compose. Gestisce il caricamento efficiente, la cache e il ritaglio (cropping/rounding) di avatar da URI locali (galleria). |
| **Dependency Injection** | **Hilt** (Dagger) / **Koin** | Gestione pulita delle dipendenze di ViewModel, Repository e Database. |
| **File I/O & Storage** | **Android Storage Access Framework (SAF)** | Uso dei contratti moderni `ActivityResultContracts.OpenDocument` e `CreateDocument` per importare ed esportare JSON senza richiedere permessi invasivi di storage. |

### 2.2 Confronto con Alternative Cross-Platform (Flutter / React Native)
* **Flutter (Dart)**: Ottima alternativa se si volesse in futuro rilasciare l'app anche su iOS con un'unica codebase. Tuttavia, per un focus prettamente Android, Jetpack Compose offre tempi di avvio minori, integrazione nativa immediata con il SAF di Android e compatibilità diretta con l'ecosistema Kotlin/Java.
* **Raccomandazione**: **Kotlin + Jetpack Compose** rimane la soluzione ottimale per fluidità, manutenibilità a lungo termine e aderenza alle linee guida Android.

---

## 3. Analisi dei Prototipi e Flusso UI

Dall'analisi visiva dei prototipi in `prototypes/`:

### 3.1 Schermata 1: Home Page (`prototypes/home-page.png`)
* **Intestazione**: Titolo stilizzato `"POCKET SHEET"` in risalto (colore corallo/rosa).
* **Elenco Personaggi (LazyColumn)**:
  * Lista di schede/banner orizzontali.
  * Ogni elemento include:
    * **Avatar**: Miniatura quadrata con bordo dell'illustrazione del personaggio.
    * **Nome del Personaggio**: Testo visibile e centrato sulla card rettangolare.
    * *(Miglioramento consigliato)*: Badge con Classe e Livello (es. `Chierico Lv. 3`) e HP correnti/massimi.
  * Tap sulla card: apre la vista di dettaglio del personaggio.
  * Pressione prolungata: menu contestuale (Rinomina, Duplica, Esporta JSON, Elimina).
* **Sezione Inferiore / FAB**:
  * Pulsante `"IMPORT JSON"` con icona di upload (`Tray + Freccia in su`).
  * Al tap, apre il selettore file di sistema per caricare un file `.json`.

### 3.2 Schermata 2: Panoramica Personaggio (`prototypes/personaggio-overview.png`)
* **Top Bar**:
  * Tasto indietro `←` per tornare alla Home.
  * Nome completo del personaggio (es. `"Elandor Dawnwhisper"`).
  * Menu opzioni in alto a destra (Esporta JSON, Modifica info di base, Reset riposo breve/lungo).
* **Sezione Immagine Profilo (Hero Section)**:
  * Grande riquadro centrale con l'illustrazione del personaggio.
  * Didascalia interattiva `"CARICA IMMAGINE"` con icona freccia upload.
  * Al tocco, apre il `PickVisualMedia` di Android per scegliere un'immagine dalla galleria o dalla fotocamera, memorizzandola localmente e associandola all'ID del personaggio.
* **Griglia di Navigazione Principale (2x2)**:
  1. 👤 **STATISTICHE**: Caratteristiche (FOR, DES, COS, INT, SAG, CAR), Modificatori, Tiri Salvezza, Abilità (Proficiencies/Expertise), Linguaggi, Competenze armature e armi.
  2. ⚔️ **COMBAT**: Punti Ferita (Attuali, Max, Temp), Dadi Vita, Tiri Salvezza contro Morte, Classe Armatura, Iniziativa, Velocità, Lista Attacchi/Armi, Gestione Slot Incantesimi e Lista Incantesimi preparati.
  3. 📜 **LORE & PRIVILEGI**: Tratti di razza, Privilegi di classe, Talenti, Allineamento, Background, Aspetto fisico, Note e Backstory.
  4. 🎒 **EQUIP**: Monete (MR, MA, ME, MO, MP), Oggetti sintonizzati (3 slot di attunement), Equipaggiamento ed inventario con quantità e pesi/note.

---

## 4. Analisi dei Dati (`Elandor_Dawnwhisper_5e.json`)

Il file JSON presenta una struttura completa per D&D 5e con alcune particolarità sui tipi di dato che l'app deve gestire con resilienza:

### 4.1 Caratteristiche del Modello Dati
1. **Campi Numerici memorizzati come Stringhe**:
   * Esempi: `"xp": "900"`, `"armor_class": "18"`, `"hp_current": "24"`, `"hp_max": "24"`, `"hit_dice_spent": "0"`, `"cp": "15"`, `"gp": "125"`.
   * *Soluzione*: Creare Type Serializers flessibili in Kotlinx Serialization che accettino sia valori numerici (`Int`) che stringhe numeriche (`"24"`), memorizzandoli nel dominio come `Int`.
2. **Formule e Dadi**:
   * Esempio: `"hit_dice_max": "3d8"`, `"damage_and_type": "1d6+2 Bludgeoning"`.
   * *Soluzione*: Mantenere il campo stringa descrittivo, con parser opzionale per l'estrazione automatica del dado (utile per calcoli o roll).
3. **Abilità con Livelli di Competenza (Tri-state)**:
   * Valori numerici: `0` = Nessuna competenza, `1` = Competenza (Proficiency), `2` = Maestria (Expertise).
   * L'interfaccia mostrerà pallini/icone a 3 stati: Vuoto ○ / Pieno ● / Doppio ◉.
4. **Slot Incantesimi Dinamici**:
   * Mappe `spell_slots_total` e `spell_slots_expended` con chiavi `"1"` fino a `"9"`.
   * Interfaccia con caselle interattive (checkbox/pip) per tracciare gli slot consumati durante la sessione di gioco.
5. **Liste di Oggetti Complessi**:
   * `weapons`: array di `{ name, atk_bonus_override, damage_and_type, notes }`.
   * `spells`: array di `{ level, name, casting_time, range, concentration, ritual, material, notes }`.

---

## 5. Architettura Tecnica del Software

```
PocketSheet/
├── app/
│   ├── src/main/java/com/pocketsheet/
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── CharacterDao.kt
│   │   │   │   ├── PocketSheetDatabase.kt
│   │   │   │   └── entity/CharacterEntity.kt
│   │   │   ├── model/
│   │   │   │   ├── Character5eJson.kt          // DTO per parsing JSON
│   │   │   │   └── serializers/FlexibleIntSerializer.kt
│   │   │   └── repository/
│   │   │       └── CharacterRepositoryImpl.kt
│   │   ├── domain/
│   │   │   ├── model/Character.kt              // Modello di dominio pulito
│   │   │   ├── rules/Rules5eCalculator.kt       // Calcolo modificatori, bonus competenza
│   │   │   └── usecase/
│   │   │       ├── ImportCharacterFromJsonUseCase.kt
│   │   │       ├── ExportCharacterToJsonUseCase.kt
│   │   │       └── UpdateCharacterUseCase.kt
│   │   └── ui/
│   │       ├── theme/ (Color, Type, Theme.kt)
│   │       ├── navigation/ (PocketSheetNavHost.kt, Destinations.kt)
│   │       ├── home/ (HomeScreen.kt, CharacterCard.kt, HomeViewModel.kt)
│   │       ├── overview/ (CharacterOverviewScreen.kt, OverviewViewModel.kt)
│   │       ├── stats/ (StatsScreen.kt, AbilityScoreCard.kt, SkillsList.kt)
│   │       ├── combat/ (CombatScreen.kt, HpTracker.kt, SpellSlotsGrid.kt)
│   │       ├── lore/ (LoreScreen.kt, FeaturesList.kt)
│   │       └── equip/ (EquipScreen.kt, CurrencyTracker.kt, InventoryList.kt)
```

### 5.1 Calcoli Automatici (Rules Engine 5e)
L'applicazione implementerà un motore di regole reattivo per calcolare automaticamente i valori derivati quando le statistiche base vengono modificate:
* **Bonus di Competenza (Proficiency Bonus)**: Calcolato dal livello ($\text{PB} = 2 + \lfloor(\text{level} - 1) / 4\rfloor$).
* **Modificatori di Caratteristica**: $\text{Mod} = \lfloor(\text{Score} - 10) / 2\rfloor$.
* **Modificatori Abilità**: $\text{Mod Statistica} + (\text{SkillLevel} \times \text{PB})$.
* **Modificatori Tiri Salvezza**: $\text{Mod Statistica} + (\text{SaveProf} ? \text{PB} : 0)$.
* **CD Incantesimi & Bonus Attacco Incantesimo**: Calcolato sulla base di `spellcasting_ability`.

---

## 6. Piano di Sviluppo a Fasi (Milestone Roadmap)

### 📌 Fase 1: Setup Progetto & Modello Dati Core
- [ ] Creazione del progetto Android (target SDK 34+, min SDK 26) con Kotlin e Jetpack Compose.
- [ ] Configurazione dipendenze: Compose Material 3, Navigation Compose, Room, Kotlinx Serialization, Coil, Hilt.
- [ ] Creazione dei DTO per il JSON (`Character5eJson`) e dei serializer flessibili per gestire numeri come stringhe.
- [ ] Implementazione e test unitari del parser su `Elandor_Dawnwhisper_5e.json`.
- [ ] Creazione del database Room e della repository locale per il salvataggio dei personaggi.

### 📌 Fase 2: Home Page & Sistema di Importazione (Prototipo 1)
- [ ] Realizzazione della schermata **Home Page** conforme a `home-page.png`:
  - Header stilizzato "POCKET SHEET".
  - Lista personaggi reattiva con miniatura avatar e nome.
  - Stati vuoti (empty state quando non ci sono personaggi).
- [ ] Implementazione del selettore file tramite `ActivityResultContracts.OpenDocument()`.
- [ ] Flusso di validazione JSON e inserimento nel database con notifica di successo/errore (Snackbar).
- [ ] Supporto per eliminazione e duplicazione del personaggio.

### 📌 Fase 3: Overview Personaggio & Gestione Avatar (Prototipo 2)
- [ ] Realizzazione della schermata **Overview Personaggio** conforme a `personaggio-overview.png`:
  - Top bar con tasto back e nome personaggio.
  - Area ritratto centrale con immagine personalizzata o segnaposto predefinito.
  - Pulsante interattivo "CARICA IMMAGINE" integrato con `ActivityResultContracts.PickVisualMedia()`.
  - Salvataggio locale del ritratto nella directory interna dell'app (`context.filesDir/avatars/`).
- [ ] Griglia 2x2 dei bottoni di navigazione per le 4 sezioni (Statistiche, Combat, Lore, Equip).

### 📌 Fase 4: Sviluppo delle 4 Aree Tematiche della Scheda
- [ ] **Schermata Statistiche**:
  - Griglia caratteristiche con punteggi e modificatori derivati automatici.
  - Elenco tiri salvezza con indicatori di competenza interattivi.
  - Tabella delle 18 abilità con visualizzazione del bonus totale e gestione 3 stati (Normale, Competente, Maestria).
  - Sezione competenze (Armature, Armi, Strumenti, Linguaggi).
- [ ] **Schermata Combat**:
  - Box HP interattivo: incrementi rapidi (+/- 1, +/- 5), danno/cura diretta, HP Temporanei.
  - Tracciatore Dadi Vita spesi/totali con pulsante "Riposo Breve".
  - Tracciatore Tiri Salvezza contro Morte (3 successi, 3 fallimenti).
  - Lista attacchi/armi con dettagli di danno e tipo.
  - Tracciatore slot incantesimi con selettori visuali per consumare/ripristinare slot di livello 1-9.
  - Lista incantesimi suddivisa per livello con filtri e tag (Concentrazione, Rituale).
- [ ] **Schermata Lore**:
  - Tratti di razza, Privilegi di classe e Talenti con rendering del testo multilinea.
  - Campi modificabili per Aspetto, Allineamento e Backstory.
- [ ] **Schermata Equip**:
  - Contatori monete (MR, MA, ME, MO, MP) con dialog di modifica rapida.
  - Gestione dei 3 slot di sintonizzazione (Attunement).
  - Inventario equipaggiamento con opzioni di aggiunta, modifica ed eliminazione oggetti.

### 📌 Fase 5: Editing Globale, Esportazione & Persistenza
- [ ] Meccanismo di salvataggio automatico (Debounced Auto-Save) ad ogni modifica effettuata dall'utente.
- [ ] Implementazione della funzionalità **"ESPORTA JSON"**:
  - Generazione del file JSON aggiornato coerente con la struttura originale.
  - Salvataggio su memoria esterna tramite `ActivityResultContracts.CreateDocument("application/json")` o condivisione rapida (Share Intent).
- [ ] Funzione di "Riposo Lungo" (Long Rest): ripristino automatico di HP, recupero dadi vita e slot incantesimi con un singolo tocco.

### 📌 Fase 6: Polish UI, Accessibilità & Test
- [ ] Rifinitura grafica e palette colori ispirata ai prototipi (accenti corallo/rosa, icone personalizzate).
- [ ] Supporto completo a schermi di diverse dimensioni (smartphone di varie risoluzioni e tablet).
- [ ] Gestione del tema scuro (Dark Theme) per sessioni di gioco notturne.
- [ ] Test di regressione sull'importazione di varianti JSON e gestione degli errori di formato.

---

## 7. Misure di Sicurezza & Best Practices Android
1. **Scoped Storage**: Nessuna richiesta di permessi pericolosi (`READ_EXTERNAL_STORAGE`). L'importazione e l'esportazione avvengono esclusivamente tramite Storage Access Framework.
2. **Copia Locale degli Avatar**: Quando l'utente seleziona un'immagine dalla galleria, l'app ne crea una copia ottimizzata nella sandbox privata dell'applicazione per garantire persistenza anche se l'immagine originale viene cancellata o spostata.
3. **Resilienza ai Crash**: Validazione rigorosa dello schema JSON all'importazione con messaggi di errore chiari all'utente in caso di file non conforme o corrotto.

---

## 8. Prossimi Passi Immediati

1. **Approvazione del Piano**: Verificare se le tecnologie raccomandate (Kotlin + Compose) e la suddivisione delle sezioni corrispondono alle aspettative.
2. **Inizializzazione del Progetto**: Creazione della struttura del repository Android (`app/build.gradle.kts`, `AndroidManifest.xml`).
3. **Implementazione del Parser & Database**: Creazione dei modelli e test su `Elandor_Dawnwhisper_5e.json`.
4. **Composizione UI delle Schermate 1 & 2**: Sviluppo della Home Page e dell'Overview del personaggio conformi ai prototipi PNG.
