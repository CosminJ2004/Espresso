IP: 3.65.147.49

# 📰 Java Reddit Clone

Un proiect backend CLI în Java care imită funcționalitățile de bază ale unui forum de discuții similar cu Reddit. Oferă autentificare, gestionarea postărilor și comentariilor, voturi și logare centralizată.

## 🎯 Scop

Acest proiect oferă un punct de pornire solid pentru aplicații de tip forum, cu Definition of Done (DoD) clar specificate pentru fiecare funcționalitate, facilitând extinderea și întreținerea.

---

## ⚙️ Funcționalități și Definition of Done

| Categorie      | Metodă                                           | Definition of Done                                                                                                                                              |
|----------------|--------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Autentificare** | `login()`                                        | - Introduce username și password<br>- Verificare în UserService<br>- Actualizare în UserContext<br>- Log INFO la succes prin LoggerManager<br>- Returnează true/false |
|                | `register()`                                     | - Introduce username și password<br>- Creează utilizator în UserService<br>- Autologin după înregistrare<br>- Returnează true dacă reușește                       |
|                | `isUserLoggedIn()`                               | - Returnează starea autentificării                                                                                                                              |
|                | `userLogout()`                                   | - Resetează starea autentificării                                                                                                                               |
| **Postări**       | `createPost()`                                   | - Introduce summary și content<br>- Creează Post<br>- Adaugă în posts<br>- Setează autorul                                                                    |
|                | `showPosts()`                                   | - Afișează toate postările cu autor și conținut                                                                                                                  |
|                | `getPostById(int id)`                           | - Returnează Post sau null + mesaj de eroare                                                                                                                    |
|                | `openPost()`                                    | - Setează currentPostID și currentPost sau afișează eroare                                                                                                      |
|                | `expandPost()`                                  | - Apelează `expand()` pe postarea curentă                                                                                                                        |
|                | `deletePost()`                                  | - Șterge dacă aparține utilizatorului curent, altfel mesaj de eroare                                                                                             |
| **Comentarii**    | `addCommentToPost()`                            | - Introduce text<br>- Creează CommentPost<br>- Adaugă în currentPost.comments și commentsAll                                                                   |
|                | `addCommentToComment()`                         | - Introduce commentID țintă și text<br>- Găsește comentariul, eroare dacă nu există<br>- Creează CommentCom și atașează răspuns în arbore                         |
| **Voturi**        | `upVoteToPost()` / `downVoteToPost()`            | - Modifică voturile postării curente<br>- Previne vot dublu<br>- Mesaj confirmare                                                                                |
|                | `upVoteToComment()` / `downVoteToComment()`      | - Modifică voturile comentariului specific<br>- Mesaj confirmare sau eroare dacă nu există comentariul                                                           |
| **Logging**      | `LoggerManager`                                  | - Gestionează ConsoleLogger și FileLogger<br>- Niveluri INFO și ERROR<br>- Respectă nivelul configurat                                                           |
|                | `ConsoleLogger`                                 | - Afișează mesaje ≥ INFO în consolă                                                                                                                              |
|                | `FileLogger`                                    | - Scrie mesaje în fișier respectând nivelul minim                                                                                                                |

---
