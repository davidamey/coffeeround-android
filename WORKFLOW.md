# Workflow For Contributing

## One Time Changes

1. Fork the repository in Github.
2. Make your changes as normal.
3. Commit changes to your working branch.
4. Push changes to Github.
5. Create a pull request via the Github web interface.
6. A project maintainer will review the changes and respond appropriately.

## Ongoing Contributions

1. Contact me to be added to the CoffeeRound Trello team.
2. Join the CoffeeRound board (auto join is enabled once you are a team member).
3. Fork the repository in Github.
4. Open in Android Studio.
5. Add Trello integration in Android Studio.
    1. Settings -> Tools -> Tasks -> Servers
    2. Add Trello.
    3. Select CoffeeRound board.
    4. Select the `Todo` list.
    5. Change the commit message to `{summary}`.
    6. Settings -> Tools -> Tasks
    7. Change changelist format and feature branch formats to `{summary}`.
6. Assign a Trello task from the Todo list to yourself (hover and press `space`).
7. Press `Alt+Shift+N`. You should see a dialogue with the list of task todo from Trello. Select a task that has been assigned to you and that you want to start working on.
9. Ensure that you create a feature branch for this (the Trello task integration dialogue will help you with this and create a branch off `master` named as the Trello task). The dialogue will also allow you to automatically move the task to the Doing list.
10. Make your changes as normal.
11. Ensure that all your changes are in the changelist associated with your Trello task in Android Studio.
12. Press `Ctrl+W` to commit changes to your working branch. This will use the Trello task summary as the commit message. The dialogue will also allow you to move the task to Pending Pull Request.
13. Push changes to Github.
14. Create a pull request via the Github web interface.
15. A project maintainer will review the changes and respond appropriately.
16. When complete they will change the status in Trello to Done.
