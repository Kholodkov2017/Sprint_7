
<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://avatars.mds.yandex.net/get-lpc/1520633/735c38f1-434d-4190-a65d-76bfd16bd2c0/width_360_q70">
    <img src="https://avatars.mds.yandex.net/get-lpc/1520633/735c38f1-434d-4190-a65d-76bfd16bd2c0/width_360_q70" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">
    Sprint 7 - Тестирование API
    <a href="https://qa-scooter.praktikum-services.ru">Сервиса по аренде скутеров</a>
</h3>

  <p align="center">
    An awesome README template to jumpstart your projects!
    <br />
    <a href="https://qa-scooter.praktikum-services.ru/docs/"><strong>Swagger сервиса »</strong></a>
    <br />
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Содержание</summary>
  <ol>
    <li>
      <a href="#about-the-project">О проекте</a>
      <ul>
        <li><a href="#built-with">Использованные технологии</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Для начала</a>
      <ul>
        <li><a href="#prerequisites">Требования</a></li>
        <li><a href="#installation">Требования для сборки</a></li>
      </ul>
    </li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## О проекте
Проект содержит набор тестов, написанных для API сервиса по аренде самокатов. Они выполняют проверку основного
функционала сервиса: 
 * возможности по регистрации и логину курьеров
 * возможность пользователю сделать заказ
 * получить пользователю список своих заказаов
 * получить пользователю корректный трек-номер для отслеживания своего заказа. 


### Использованные технологии

Использованные технологии: 

* [![Java11][java11]][java-url]
* [![junit][junit]][junit]
* [![maven][maven]][maven-url]
* [![allure][allure]][allure-url]

<p align="right">(<a href="#readme-top">к верху страницы</a>)</p>



<!-- GETTING STARTED -->
## Для начала

Для того, чтобы успешно установить проект, вы должны выполнить следующие предусловия 

### 

В первую очередь необходимо установить java 11 Amazon Сorretto 

В `Windows` это можно сделать с помощью команды
* `chocolatey`
  ```shell
  choco install corretto11jdk
  ```
Установка maven в `Windows` 
* `chocolatey`
  ```shell
  choco install maven
  ```
### Установка

1. Клонируем репозиторий
   ```sh
   git clone https://github.com/Kholodkov2017/Sprint_7.git
   ```
2. Открываем `IntelliJ IDEA` и выполняем команду
   ```sh
   mvn clean test 
   ```
3. Чтобы запустить сервер `Allure` для просмотра результатов тестирования выполняем комманду
   ```shell
    mvn alure:serve
   ```

<p align="right">(<a href="#readme-top">К верху страницы</a>)</p>






<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[java11]: https://img.shields.io/badge/java-11-brightgreen
[java-url]: https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html

[java11]: https://img.shields.io/badge/java-11-brightgreen
[java-url]: https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html

[junit]: https://img.shields.io/badge/JUnit-4.13.2-brightgreen
[junit-url]: https://mvnrepository.com/artifact/junit/junit/4.13.2

[maven]: https://img.shields.io/badge/maven-3.9.0-brightgreen
[maven-url]: https://maven.apache.org/docs/3.9.0/release-notes.html

[restassured]: https://img.shields.io/badge/restassured-5.3.0-brightgreen
[restassured-url]: https://mvnrepository.com/artifact/io.rest-assured/rest-assured/5.3.0

[allure]: https://img.shields.io/badge/allure-5.3.0-brightgreen
[allure-url]: https://github.com/allure-framework/allure2/releases