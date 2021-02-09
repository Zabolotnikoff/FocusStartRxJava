Focus Start

Реактивное программирование.
Заданияе.
Небольшой рассказ в текстовом виже отобразить его в скролл-вью.
1. Реализовать строку поиска, на которую будет подписан observer (т.е. без кнопки завершения ввода).
2. При вводе текста в строку поиска, должен производиться поиск совпадений по тексту. Количество совпадений необходимо вывести в отдельную текствью.
3. Поиск должен работать не в основном потоке, чтобы во время поиска приложение не лагало.
4. Автоматический старт поиска после задержки ввода в строку поиска в 0.7 секунды.
*5. задание со звёздочкой. Представить текст как поток слов, и обновлять текствью счётчика сразу при каждом новом совпадении.

В программе реализованы сразу два счётчика, статический и динамический (задание со звёздочкой). Результаты выведен на одной странице для визуализации различий их работы. 
Компьютер у меня не самый быстрый, поэтому разницу работы счётчиков мне хорошо видно. Если у вас она не будет заметна, возьмите текст бОльшего размера.

Т.к. алгоритмы поиска у счётчиков отличаются (статический считает подствроки в тексте, динамический ищет слова содержащие подстроку) на некоторых наборах данных счётчики будут 
указывать разные значения (к примеру, если строка поиска несколько раз входит в какие-либо слова, либо содержит разделители слов). Данное обстоятельство не является ошибкой, 
т.к. основной задачей была реализация реактивного подхода в программировании.