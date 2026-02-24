# Scriptest
Android фреймворк для UI и UX тестирования на основе исполняемых сценариев.

### Интеграция в приложение
#### 1. Foreground Service
В манифест приложения необходимо добавить разрешения для запуска Foreground Service

`<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />`

`<uses-permission android:name="android.permission.FOREGROUND_SERVICE_DATA_SYNC" />`

А также сам Foreground Service

```xml
<service
        android:name="dev.firecrown.scriptest.services.ScriptestService"
        android:foregroundServiceType="dataSync"
        android:exported="false"
        tools:ignore="ForegroundServicePermission" />

```
Сервис необходим фреймворку для стабильной долговременной работы во время исполнения сценариев тестирования.

#### 2. Класс Scriptest

Запуск сценариев происходит через класс `Scriptest()` и operator метод `invoke()`, в конструктор класса необходимо передать `context: Context`,
а в конструктор метода `script: String` и `output: Output`.

Enum класс Output определяет режим экспорта результата:

`Output.DEFAULT` - Функция invoke() возвращает результат в типе `String`.

`Output.SAVE` - Сохраняет результат в виде json файла в папку /scripts в директории приложения.


##### Пример реализации запуска сценария тестирования с возвращением результата в переменную `resultJson`:
``` kotlin
scope.apply{ // Ваш Coroutine Scope
    val scriptest = async {
                    return@async Scriptest(context)(
                        script = scriptJson, // Ваша переменная содержащая сценарий в формате json
                        output = Output.SAVE
                    )
                }
    launch {
        val resultJson = scriptest.await()
    }
}
```

Класс `Scriptest()` содержит обработчик `UncaughtExceptionHandler` для сохранения файла при появлении необработанного исключения в тестируемом приложении,
не рекомендуется создавать отдельный обработчик во избежание потери результата при падении приложения.


#### 3. Функция ScriptestOverlay

Для отбражения графического наложения для пользователя/тестировщика используется Composable фукнция `ScriptestOverlay()`
она имеет аргумент в виде лямбда-функции `content()` в нее необходимо поместить всю композицию тестируемого ComposeView.

``` kotlin
setContent {
            ScriptestOverlay {
                // Ваш UI
            }
        }
```

#### 4. Класс TestLog

Для логирования используется класс `TestLog` и его статические методы содержащие параметры `script: String` и `text: String`
В параметре `script: String` нужно указать название сценария которое в нем прописано для привязки лога к определенному скрипту.
В параметре `text: String` нужно указать само сообщение которое вы хотите внести в журнал.

Типы логов:


`TestLog.debug()` - сообщение для отладки.


`TestLog.error()` - сообщение об ошибке.


`TestLog.info()` - информационное сообщение.


### Написание скриптов тестирования

Скрипты тестирования пишутся в формате json.
Первым параметром необходимо указать название сценария.

`"name": "Название"`

После него идет параметр `script` это массив "блоков" выполнения тестирования.

Структура блока тестирования:
```json
{
      "title": "Заголовок",
      "text": "Информация/Указания действий тестировщика",
      "dialogOverlayPosition": "Center", // Расположение диалогового окна по вертикали (Center, Top, Bottom)
      "textField": "Заголовок текстового поля (Опционально, если поля ввода не требуется то null)",
      "pointer": [0.7, 0.0, 0.6, 0.2] // Расположение указателя на область взаимодействия с экраном в виде отступов от краев
                                      [Start, End, Top, Bottom] в формате относительных единиц от 0.0 до 1.0
                                      (Опционально, если указатель не требуется то null),
      "options": ["Да", "Нет"] // Варианты ответа для тестировщика (Опционально, если варианты не требуются то null),
      "snapshot": false // Сохранить состояние композиции на момент выполнение блока (true/false)
}

```


Пример полного скрипта тестирования:
```json
{
  "name": "CustomButtonTest",
  "script": [
    {
      "title": "Переход на экран",
      "text": "Перейдите на экран профиля",
      "dialogOverlayPosition": "Center",
      "textField": null,
      "pointer": [0.7, 0.0, 0.6, 0.2],
      "options": null,
      "snapshot": false
    },
    {
      "title": "Переход на экран",
      "text": "У вас получилось перейти на экран профиля?",
      "dialogOverlayPosition": "Center",
      "textField": null,
      "pointer": null,
      "options": ["Да", "Нет"],
      "snapshot": true
    },
    {
      "title": "Нажатие на кнопку",
      "text": "Нажмите на кнопку Настройки",
      "dialogOverlayPosition": "Bottom",
      "textField": "Что произошло после нажатия?",
      "pointer": [0.3, 0.3, 0.3, 0.5],
      "options": null,
      "snapshot": true
    }
  ]
}

```

Пример результата выполнения скрипта:

```json
{
  "name": "CustomButtonTest",
  "result": [
    {
      "textField": null,
      "snapshot": null,
      "option": null,
      "exceptionMessage": null,
      "timestamp": "2026-02-17 15:30:00"
    },
    {
      "textField": null,
      "snapshot": "iVBORw0KGgoAAAANSUhEUgAAABQAAAARCAYAAAD==",
      "option": "Да",
      "exceptionMessage": null,
      "timestamp": "2026-02-17 15:31:00"
    },
    {
      "textField": "Нажал на кнопку Настройки, открылось меню с опциями",
      "snapshot": "iVBORw0KGgoAAAANSUhEUgAAABQAAAARCAYAAAD==",
      "option": null,
      "exceptionMessage": null,
      "timestamp": "2026-02-17 15:32:00"
    }
  ],
  "logs": [
    {
      "text": "Displayed overlay at center for block 1, pointer [0.7,0.0,0.6,0.2]",
      "timestamp": "2026-02-17 15:30:01",
      "type": "DEBUG"
    },
    {
      "text": "User selected option 'Да' for block 2, snapshot saved",
      "timestamp": "2026-02-17 15:31:05",
      "type": "INFO"
    },
    {
      "text": "Text input received for block 3: 'Нажал на кнопку Настройки, открылось меню с опциями', snapshot saved",
      "timestamp": "2026-02-17 15:32:10",
      "type": "DEBUG"
    }
  ]
}


```









