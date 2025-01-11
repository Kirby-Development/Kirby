# Kirby API

**Kirby API** è un framework estendibile progettato per la costruzione e gestione di plugin su piattaforma Minecraft tramite l'API Paper. Offre una struttura modulare per il caricamento, la gestione e la registrazione di plugin, oltre a un sistema di servizi flessibile.

## Funzionalità Principali

1. **Sistema di Registrazione Plugin**:
    - I plugin possono essere registrati automaticamente tramite l'estensione della classe astratta `KirbyPlugin`.
    - La classe `APIRegister` gestisce i plugin installati permettendo la loro registrazione e recupero.

2. **Sistema di Gestione dei Servizi**:
    - I servizi possono essere registrati e recuperati tramite chiavi uniche definite dalla classe `ServiceKey`.
    - La gestione dei servizi è centralizzata nella classe `ServiceManager`.

3. **Compatibilità con l'API Paper**:
    - Combina l'API di Paper (per i server Minecraft) con il supporto per un'architettura modulare.

4. **Integrazione di Lombok**:
    - Utilizza Lombok per minimizzare il codice boilerplate.

---

## Struttura del Progetto

- **KirbyApi**
    - Modulo principale per la gestione dei plugin e dei servizi.
    - Contiene:
        - `APIRegister`: Per la gestione dei plugin registrati.
        - `ServiceManager`: Per la gestione dei servizi interni.

- **KirbyScreenShare**
    - Plugin dimostrativo che estende `KirbyPlugin` e rappresenta un esempio di implementazione di un plugin che utilizza KirbyApi.

- **Kirby**
    - Plugin principale, anche questo estende `KirbyPlugin`.

---

### Classe Principale: KirbyApi

La classe `KirbyApi` è il punto d'ingresso per i servizi principali del framework:
- **ServiceManager**: Gestisce i servizi registrati condivisi tra i plugin.
- **APIRegister**: Carica i plugin che implementano `IKirby`.

#### Esempio
```java
KirbyApi.getRegister().install(new MyKirbyPlugin());
```

---

## Guida all'Uso

### 1. Creare un Plugin
Per creare un plugin con KirbyApi, segui questi passi:

1. Crea una classe plugin che estenda `KirbyPlugin` e fornisca il nome del plugin al costruttore.

   ```java
   public final class ExamplePlugin extends KirbyPlugin {
       
       public ExamplePlugin() {
           super("ExamplePlugin");
       }
       
       @Override
       public void onEnable() {
           // Codice da eseguire quando il plugin si abilita
       }
   }
   ```

2. Registra il plugin nel file `plugin.yml`:
   ```yaml
   name: ExamplePlugin
   version: '1.0'
   main: dev.kirby.example.ExamplePlugin
   api-version: '1.20'
   ```

---

### 2. Registrare Servizi
Puoi registrare e recuperare servizi attraverso `ServiceHelper` o utilizzando direttamente `ServiceManager`.

```java
getManager().put(ServiceKey.key(MyService.class), new MyService());
MyService service = getManager().get(MyService.class);
```

---

### 3. Utilizzo del Sistema di Registrazione
I plugin possono essere registrati usando `IKirby` e gestiti tramite `APIRegister`.

```java
IKirby plugin = getRegister().get("Keeby");
if (plugin != null) {
    // Plugin trovato e registrato
}
```

---

## Requisiti

- **Java**: 21 o superiore
- **Maven**: Per gestire le dipendenze e la build
- **Dipendenze Plugin**: Paper API, Lombok

Assicurati che nel tuo file `pom.xml` siano elencate le seguenti dipendenze:

```xml
<dependencies>
    <dependency>
        <groupId>io.papermc.paper</groupId>
        <artifactId>paper-api</artifactId>
        <version>1.20.4-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.30</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

## Build del Progetto

1. Esegui `mvn clean package` per impacchettare i jar del plugin.
2. I jar saranno generati nella cartella specificata nel pom.xml.

---

## Contribuzione

Se vuoi contribuire al progetto:
1. Crea un fork di questo repository.
2. Fai le modifiche e invia una pull request.
3. Assicurati di seguire le linee guida di codifica.

---

## Autori

- **SweetyDreams_** (Autore del progetto principale e plugin demo)

---

## Licenza

Il progetto è distribuito sotto una licenza aperta. Consulta il file `LICENSE` per maggiori dettagli.