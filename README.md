<p align="center">
  <img src="https://raw.githubusercontent.com/PKief/vscode-material-icon-theme/ec559a9f6bfd399b82bb44393651661b08aaf7ba/icons/folder-markdown-open.svg" width="100" alt="project-logo">
</p>
<p align="center">
    <h1 align="center">CODRAW</h1>
</p>
<p align="center">
    <em>Draw together, unite creativity in real-time.</em>
</p>
<p align="center">
	<img src="https://img.shields.io/github/license/Desuche/coDraw?style=default&logo=opensourceinitiative&logoColor=white&color=0080ff" alt="license">
	<img src="https://img.shields.io/github/last-commit/Desuche/coDraw?style=default&logo=git&logoColor=white&color=0080ff" alt="last-commit">
	<img src="https://img.shields.io/github/languages/top/Desuche/coDraw?style=default&color=0080ff" alt="repo-top-language">
	<img src="https://img.shields.io/github/languages/count/Desuche/coDraw?style=default&color=0080ff" alt="repo-language-count">
<p>
<p align="center">
	<!-- default option, no dependency badges. -->
</p>

<br><!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary><br>

- [ Overview](#-overview)
- [ Features](#-features)
- [ Repository Structure](#-repository-structure)
- [ Modules](#-modules)
- [ Getting Started](#-getting-started)
  - [ Installation](#-installation)
  - [ Usage](#-usage)
  - [ Tests](#-tests)
- [ Project Roadmap](#-project-roadmap)
- [ Contributing](#-contributing)
- [ License](#-license)
- [ Acknowledgments](#-acknowledgments)
</details>
<hr>

##  Overview

CoDraw is a collaborative drawing application that enables real-time group painting and communication. The project encompasses features such as chat messaging, color selection, studio creation, and file sharing among users. With components like internal and external servers, network utilities, and a user-friendly UI, CoDraw fosters interactive drawing experiences. The softwares value proposition lies in its ability to facilitate seamless collaboration and creativity through shared canvas interactions.

---

##  Features

|    |   Feature         | Description |
|----|-------------------|---------------------------------------------------------------|
| ⚙️  | **Architecture**  | The project architecture follows a modular design with clear separation of concerns. It consists of core components managing UI, server connections, and message handling. Internal and proxy servers handle collaborative painting functionalities efficiently. |
| 🔩 | **Code Quality**  | The codebase maintains high quality and follows consistent coding style conventions. It demonstrates good practices such as encapsulation, proper commenting, and adherence to design patterns. |
| 📄 | **Documentation** | The project has extensive documentation covering the functionality of various components and their interactions. This documentation enhances understanding and aids developers in contributing effectively to the project. |
| 🔌 | **Integrations**  | Key integrations include network utilities for IP handling, server discovery services, and communication protocols for real-time collaborative drawing. External dependencies for GUI components and network operations are effectively integrated. |
| 🧩 | **Modularity**    | The codebase exhibits high modularity, enabling easy maintenance and extension of functionalities. Components such as UI elements, server handlers, and message processors are well encapsulated and reusable. |
| 🧪 | **Testing**       | Testing frameworks and tools used for the project ensure robust functionality and stability. Unit testing and integration testing are likely integrated into the development process. |
| ⚡️  | **Performance**   | The project demonstrates efficient resource management and real-time capabilities for collaborative drawing. Optimized socket connections, message handling, and UI updates contribute to a smooth user experience. |
| 🛡️ | **Security**      | Security measures include encoding utilities for secure data exchange and error handling for graceful exits. The project shows considerations for data protection during communication and file sharing. |
| 📦 | **Dependencies**  | Key external libraries and dependencies utilized include Java for core functionalities. These dependencies play a crucial role in enabling network operations, UI rendering, and message handling. |

---

##  Repository Structure

```sh
└── coDraw/
    ├── KidPaintLatest.iml
    ├── color-spectrum.jpg
    └── src
        └── org
```

---

##  Modules

<details closed><summary>src.org</summary>

| File                                                                             | Summary                                                                                                                                                                                                               |
| ---                                                                              | ---                                                                                                                                                                                                                   |
| [CoDraw.java](https://github.com/Desuche/coDraw/blob/master/src/org/CoDraw.java) | Initializes and starts the CoDraw application by creating a Core instance with specific dimensions. Logs version information and initiates the core functionality for a drawing application in the CoDraw repository. |

</details>

<details closed><summary>src.org.core</summary>

| File                                                                              | Summary                                                                                                                                                                                                                                                  |
| ---                                                                               | ---                                                                                                                                                                                                                                                      |
| [Core.java](https://github.com/Desuche/coDraw/blob/master/src/org/core/Core.java) | Manages UI elements, user interaction, and server connections within the CoDraw application. Controls drawing data, UI visibility, and server status, with functions to restart components, handle server offline scenarios, and save drawings to files. |

</details>

<details closed><summary>src.org.servers</summary>

| File                                                                                                   | Summary                                                                                                                                                                                                                                                           |
| ---                                                                                                    | ---                                                                                                                                                                                                                                                               |
| [ServerMessage.java](https://github.com/Desuche/coDraw/blob/master/src/org/servers/ServerMessage.java) | Defines server message types for the coDraw project, organizing main communication entities. Allows easy retrieval based on message value, enhancing server interaction clarity and efficiency.Integral for message handling within the repositorys architecture. |

</details>

<details closed><summary>src.org.servers.internalserver</summary>

| File                                                                                                                    | Summary                                                                                                                                                                                                |
| ---                                                                                                                     | ---                                                                                                                                                                                                    |
| [InternalServer.java](https://github.com/Desuche/coDraw/blob/master/src/org/servers/internalserver/InternalServer.java) | Implements an internal server handling various message types for collaborative painting. Manages socket connections, message distribution, and file sharing among subscribers in a distributed system. |
| [MessageHandler.java](https://github.com/Desuche/coDraw/blob/master/src/org/servers/internalserver/MessageHandler.java) | Handles chat, pixel, bucket, data load, file, and image messages in a real-time collaborative drawing app. Updates UI and broadcasts messages to subscribers, promoting interactivity among users.     |

</details>

<details closed><summary>src.org.servers.proxyserver</summary>

| File                                                                                                                                   | Summary                                                                                                                                                                                                                         |
| ---                                                                                                                                    | ---                                                                                                                                                                                                                             |
| [MessageHandler.java](https://github.com/Desuche/coDraw/blob/master/src/org/servers/proxyserver/MessageHandler.java)                   | Processes chat, pixel, bucket, image, file, and data load messages by interacting with UI methods and ChatArea for display.                                                                                                     |
| [ExternalConnectedServer.java](https://github.com/Desuche/coDraw/blob/master/src/org/servers/proxyserver/ExternalConnectedServer.java) | Enables communication with an external server for collaborative drawing. Facilitates sending chat messages, drawing pixels and buckets, and uploading files and images. Handles incoming messages from the server in real-time. |

</details>

<details closed><summary>src.org.utils</summary>

| File                                                                                                 | Summary                                                                                                                                                                        |
| ---                                                                                                  | ---                                                                                                                                                                            |
| [EncodingUtils.java](https://github.com/Desuche/coDraw/blob/master/src/org/utils/EncodingUtils.java) | Encodes and decodes IP addresses and ports as Base64 strings, facilitating secure data exchange in the coDraw repository structure.                                            |
| [NetworkUtils.java](https://github.com/Desuche/coDraw/blob/master/src/org/utils/NetworkUtils.java)   | Finds free TCP/UDP ports within defined limits, fetches public IP using multiple endpoints. Logs activities using a global logger. Features error handling and graceful exits. |

</details>

<details closed><summary>src.org.gui</summary>

| File                                                                         | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| ---                                                                          | ---                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| [UI.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/UI.java) | The `UI.java` file in the `coDraw` repositorys `src/org/gui/` directory primarily handles the user interface components for the collaborative drawing application. It integrates functionalities such as chat area display, color picking, username input, and communication with internal and external servers. The file orchestrates event listeners for user interactions and incorporates graphical rendering for smooth drawing experiences. Additionally, it includes utilities for network operations and logging. Overall, `UI.java` plays a crucial role in providing a seamless and interactive interface for users to collaborate on drawing activities within the larger `coDraw` application architecture. |

</details>

<details closed><summary>src.org.gui.peripherials</summary>

| File                                                                                                            | Summary                                                                                                                                                                                                                                      |
| ---                                                                                                             | ---                                                                                                                                                                                                                                          |
| [UserNameInput.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/peripherials/UserNameInput.java) | Enables users to input and store their username. Displays a dialog box with input field, Continue button, and feedback messages. Communicates entered username to parent UI. Supports visibility functionality with a separate UI component. |
| [ColorPicker.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/peripherials/ColorPicker.java)     | Implements a color picker GUI component for selecting colors from an image. Utilizes a singleton design pattern to ensure a single instance. Integrates with parent GUI for color selection.                                                 |

</details>

<details closed><summary>src.org.gui.studioselection</summary>

| File                                                                                                                                 | Summary                                                                                                                                                                                                                                      |
| ---                                                                                                                                  | ---                                                                                                                                                                                                                                          |
| [StudioSelectionService.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/studioselection/StudioSelectionService.java) | Facilitates server interactions, launches users studio, and processes invitation codes. Handles UI components and manages server connection tasks within the Studio Selection service of the application.                                    |
| [StudioSelectionUI.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/studioselection/StudioSelectionUI.java)           | Implements a studio selection user interface, facilitating server discovery and enabling the user to join or start studios. Designed with clear studio selection buttons and features like using invite codes and starting personal studios. |

</details>

<details closed><summary>src.org.gui.chat</summary>

| File                                                                                                | Summary                                                                                                                                                                                                                                                                       |
| ---                                                                                                 | ---                                                                                                                                                                                                                                                                           |
| [BannerAlert.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/chat/BannerAlert.java) | Creates a customizable banner alert with text content, yellow background, and padding for the repositorys GUI chat module.                                                                                                                                                    |
| [ChatItem.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/chat/ChatItem.java)       | Defines a base class for chat items with dynamic styling based on message sender. Automatically adjusts message display position and appearance. Facilitates consistent chat item layout within the GUI.                                                                      |
| [ChatImage.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/chat/ChatImage.java)     | Generates and displays chat images with an optional download option. Handles image loading, scaling, and error handling. Enhances user experience with message timestamps and save functionality. Complements the chat interface for an interactive communication experience. |
| [ChatFile.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/chat/ChatFile.java)       | Implements a chat interface for file sharing, allowing users to view and save files seamlessly within the chat platform. Integrates download functionality, enhancing user experience by simplifying file management and interactions.                                        |
| [ChatArea.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/chat/ChatArea.java)       | Manages chat messages, file uploads, images, and alerts in the GUI. Implements dynamic message positioning and scrolling functionality for real-time chat updates.                                                                                                            |
| [ChatUtils.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/chat/ChatUtils.java)     | Enables downloading chat files and images within the ChatArea interface by providing file saving functionality with user-selected file formats and error logging.                                                                                                             |
| [ChatMessage.java](https://github.com/Desuche/coDraw/blob/master/src/org/gui/chat/ChatMessage.java) | Creates a styled chat message component with sender information and message content for the GUI chat feature. Displays messages in a JTextArea with specified dimensions, fonts, colors, and word wrap settings.                                                              |

</details>

<details closed><summary>src.org.discovery</summary>

| File                                                                                                                                 | Summary                                                                                                                                                                                                                  |
| ---                                                                                                                                  | ---                                                                                                                                                                                                                      |
| [DiscoveryBroadcastListener.java](https://github.com/Desuche/coDraw/blob/master/src/org/discovery/DiscoveryBroadcastListener.java)   | Enables broadcasting server availability, responding to specific requests by sending server details. Facilitates seamless communication between KidPaint servers and clients on the network.                             |
| [DiscoveryService.java](https://github.com/Desuche/coDraw/blob/master/src/org/discovery/DiscoveryService.java)                       | Enables launching and managing discovery services. Provides methods to kill, launch listener, send broadcasts, and retrieve server information. Critical for network discovery functionalities in the coDraw repository. |
| [DiscoveryRequestBroadcaster.java](https://github.com/Desuche/coDraw/blob/master/src/org/discovery/DiscoveryRequestBroadcaster.java) | Implements discovery request broadcasting with UDP sockets. Finds servers on the network and stores their details.                                                                                                       |

</details>

---

##  Getting Started

**System Requirements:**

* **Java**: `version x.y.z`

###  Installation

<h4>From <code>source</code></h4>

> 1. Clone the coDraw repository:
>
> ```console
> $ git clone https://github.com/Desuche/coDraw
> ```
>
> 2. Change to the project directory:
> ```console
> $ cd coDraw
> ```
>
> 3. Install the dependencies:
> ```console
> $ mvn clean install
> ```

###  Usage

<h4>From <code>source</code></h4>

> Run coDraw using the command below:
> ```console
> $ java -jar target/myapp.jar
> ```

###  Tests

> Run the test suite using the command below:
> ```console
> $ mvn test
> ```

---

##  Project Roadmap

- [X] `► INSERT-TASK-1`
- [ ] `► INSERT-TASK-2`
- [ ] `► ...`

---

##  Contributing

Contributions are welcome! Here are several ways you can contribute:

- **[Report Issues](https://github.com/Desuche/coDraw/issues)**: Submit bugs found or log feature requests for the `coDraw` project.
- **[Submit Pull Requests](https://github.com/Desuche/coDraw/blob/main/CONTRIBUTING.md)**: Review open PRs, and submit your own PRs.
- **[Join the Discussions](https://github.com/Desuche/coDraw/discussions)**: Share your insights, provide feedback, or ask questions.

<details closed>
<summary>Contributing Guidelines</summary>

1. **Fork the Repository**: Start by forking the project repository to your github account.
2. **Clone Locally**: Clone the forked repository to your local machine using a git client.
   ```sh
   git clone https://github.com/Desuche/coDraw
   ```
3. **Create a New Branch**: Always work on a new branch, giving it a descriptive name.
   ```sh
   git checkout -b new-feature-x
   ```
4. **Make Your Changes**: Develop and test your changes locally.
5. **Commit Your Changes**: Commit with a clear message describing your updates.
   ```sh
   git commit -m 'Implemented new feature x.'
   ```
6. **Push to github**: Push the changes to your forked repository.
   ```sh
   git push origin new-feature-x
   ```
7. **Submit a Pull Request**: Create a PR against the original project repository. Clearly describe the changes and their motivations.
8. **Review**: Once your PR is reviewed and approved, it will be merged into the main branch. Congratulations on your contribution!
</details>

<details closed>
<summary>Contributor Graph</summary>
<br>
<p align="center">
   <a href="https://github.com{/Desuche/coDraw/}graphs/contributors">
      <img src="https://contrib.rocks/image?repo=Desuche/coDraw">
   </a>
</p>
</details>

---

##  License

This project is protected under the [SELECT-A-LICENSE](https://choosealicense.com/licenses) License. For more details, refer to the [LICENSE](https://choosealicense.com/licenses/) file.

---

##  Acknowledgments

- List any resources, contributors, inspiration, etc. here.

[**Return**](#-overview)

---
