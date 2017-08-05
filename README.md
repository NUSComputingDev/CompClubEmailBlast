# CompClubEmailBlast

A web browser GUI to help the NUS Students' Computing Club send HTML emails to the Computing Club students.

## Getting Started

See installation for notes on how to use this app on your computer.

### Prerequisites

- Computer (almost any operating system under the sun will do)
- Node.js ([Download link](https://nodejs.org/en/download/))
- Git
  - Windows: Git Bash ([Download link](https://git-scm.com/downloads))
  - Linux: Git CLI ([Installation commands](https://git-scm.com/download/linux))
  - Mac: Git Xcode CLI tools or other methods ([See here](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git))

### Installing

Below are the steps to installing and launching the CompClubEmailBlast web browser GUI app. If your look a little further down, we have translated the steps to actual Git Bash/Terminal commands. Please enter those commands into your Git Bash/Terminal in order.

1. Open Git Bash/Terminal
2. Add Node.js to `PATH` variable
  - This is so that you can run the `npm` command from any directory or location in your computer even though the executable is at a single location elsewhere
3. Navigate to a known location in your computer (e.g. `Documents` folder)
4. Clone this repository to that known location
  - So the contents will end up inside `Documents/CompClubEmailBlast/`
5. Install the packages that the app needs to execute
  - The packages are not included in this repository, so we should grab the latest ones from online
6. Start the app!

Windows Git Bash:

    PATH=$PATH:C:\Program Files\nodejs
    cd Documents # if this fails, try `cd /c/Users/<your_username>/Documents/`
    git clone https://github.com/NUSComputingDev/CompClubEmailBlast.git
    cd CompClubEmailBlast
    npm install
    ./build.sh start
    
Linux Terminal (Mac should be somewhat similar too):

    cd Documents
    git clone https://github.com/NUSComputingDev/CompClubEmailBlast.git
    cd CompClubEmailBlast
    npm install
    npm run start

## Authors

* **Thenaesh Elango** - *Initial work* - [thenaesh](https://github.com/thenaesh)
