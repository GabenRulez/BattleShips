# Dokumentacja - m3


## Wprowadzenie rankingu

Po zakończeniu gry, przedstawiany jest ekran podsumuwujący rozgrywkę, na którym zamieszczono obecną ilość punktów, ich zmianę oraz ranking.

Ponieważ przy porażce punkty gracza są odejmowane postanowiliśmy, że początko każdy gracz będzie miał 1000 punktów. Oprócz tego dodaliśmy możliwość resetowania liczby punktów dla wszystkich graczy.

Ranking każdego gracza jest przechowwywany w tabeli Player w bazie danych.

Obsługa rankingu została zrealizowanan przez przez **Jacka Nitychoruka**.

### Widok po zakończeniu gry
![](ss09.png)

### Ekran porażki
![](ss10.png)

### Zakończenie gry na poziomie łatwym
![](ss11.png)

### Zakończenie gry na poziomie średnim
![](ss12.png)

### Zakończenie gry na poziomie trudnym
![](ss12.png)

## Dodanie tooltipów z pomocą kontekstową

W widoku dodaliśmy do przycisków tooltipy z informacjami o tym, co wykonują. W tym celu skorzystaliśmy z dostępnej w bibliotece JavaFX klasie Tooltip, które podłączyliśmy do przycisków.


Za tą część odpowiedzialny był **Paweł Kiełbasa**.

### Tooltip do losowania planszy
![](ss14.png)

### Tooltip do obracania statku
![](ss13.png)

### Tooltip do cofania operacjii
![](ss20.png)

### Tooltip do powtarzania operacji
![](ss19.png)

### Tooltip do uruchomienia gry
![](ss16.png)

## Stworzanie usługi wysyłania maily do użytkownika

W naszej grze przyjeliśmy założenie, że mail zostaje wysłany do wszystkich użytkowników, którzy zostali pobici przez danego gracza w rozegranej grze. Do wysyłania maili wykorzystaliśmy protokół SMTP. Ze wzgledu na mocne spowolnienie działania programu przez tą funkcję postanowiliśmy wykonywać ją w osobnym watku. Oprócz tego usunęliśmy wszystkie polskie znaki ze względu na brak obsługi ich.

Za stworzenie kodu do wysyłania maili odpowiedzialny był **Wojciech Kosztyła**.

### Przykładowy mail z informacją o byciu pobitym
![](ss21.png)

## Dodanie możliwości losowego wybrania położenia statków

Do głownego ekranu gry dodaliśmy przycisk generujący nam losowe rozmieszczenie statków gracza na planszy. W tym przypadku główne metody znajdują się w klasie BoardInitializer, która pozwala nam na storzenie statku o dostępnej i losowej długości w dostępnym miejscu.

Odpowiedzialny za te funkcje był **Jacek Nitychoruk**.

### Przykładowo wygenerowana losowa plansza
![](ss22.png)

## Dodanie obsługi gry myszką

Aby ułatwić korzystanie z gry, do przycisków na myszcze dodaliśmy dwie funkcje: środkowy przycisk powoduje obrócenie wybranego staku natomiast prawy przycisk usuwa statek na który najechaliśmy.

Odpowiedzialny za te funkcje był **Jacek Nitychoruk**.

## Dodanie wizualizacji obecnie umieszczanego statku

Aby poprawić identyfikację, gdzie stawiany jest statek dodaliśmy wyświetlanie pozycji statku. Aby to wykonać musieliśmy stworzyć funcję odświeżającą planszę gracza.

Odpowiedzialny za te funkcje był **Jacek Nitychoruk**.
