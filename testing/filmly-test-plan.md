# FILMLY WEB TEST PLAN

## 1. Product analysis

### Product overview
FilmLy is a web application designed to help users quickly find movies they are likely to enjoy.  
The product reduces the time users spend searching for interesting films by offering movie recommendations based on a title they already like, as well as additional filtering options and recommended sections on the Main page.  
Instead of browsing through large catalogs manually, users can enter a movie name, select recommended sections, or use filters to discover relevant movies faster and more conveniently.

### Target audience
- Users who want to quickly find a movie for watching;
- People who do not want to spend a lot of time browsing movie lists;
- Users who already know one movie they liked and want similar recommendations;
- Users who prefer searching by filters such as genre, mood, or other categories.

### Users’ problems
- Users spend too much time searching for a movie to watch;
- Users may not know what to watch next;
- Large movie catalogs can make the selection process difficult and overwhelming;
- Users want a faster way to find movies similar to those they already enjoy;
- Users need a more convenient way to narrow movie options based on preferences.

---

## 2. Test strategy

### Scope of testing

#### In-scope features
- Sign in page;
- Sign up page;
- Main / Home page;
- Film list page;
- Film overview page;
- FEATURE / Filter. ????????????

### Testing types
- Testing with test cases; ???????????
- Exploratory testing;
- Functional testing;
- Permission Testing; ????????????
- Usability Testing (verification of layout, responsiveness, and visual consistency);
- Compatibility Testing (Chrome, Edge (might be mobile));
- Smoke testing;
- Feature testing;
- Negative testing (invalid inputs, boundary values, incorrect flows).

### Risks

#### Functional risks
- The system may return irrelevant or incorrect movie recommendations based on the entered title;
- Search results may not match the user’s request;
- Filters may work incorrectly or return incomplete/incorrect results; ?????
- The system may fail to display similar movies for valid input;
- Some movie data may be missing, duplicated, or displayed incorrectly.

#### Usability risks
- Users may still spend too much time searching for a movie if recommendations or filters are not convenient enough;
- The interface may be confusing, and users may not understand how to search by title or apply filters;
- The filtering process may require too many actions, reducing the overall user experience.

#### Business risks
- If recommendations are poor, users may lose trust in the product and stop using it;
- If the product does not actually reduce movie search time, it may fail to achieve its main business goal;
- The product name may already be in use by another company, brand, or platform, which may create legal, branding, or market confusion risks.

#### Other risks
- Movie titles may be processed incorrectly due to spelling variations, language differences, or special characters;
- The application may not handle cases where a movie is not found in the database;
- Search or recommendation results may load too slowly;
- The system may become unstable under increased number of users or requests;
- Filtering large amounts of movie data may affect response time;
- UI elements such as search fields, buttons, or filters may display incorrectly on smaller screens.

### Test logistics
- Jira — defect tracking and feature stories;
- TestRail — test case management;
- Chrome DevTools.

---

## 3. Test objectives
- Verifying that users can successfully search for a movie by title;
- Verifying that the system returns relevant similar movie recommendations;
- Verifying that filters work correctly and narrow results according to selected criteria;
- Verifying that the application handles valid, invalid, and empty input properly;
- Verifying that movie information is displayed correctly and consistently;
- Verifying that the product supports the main business goal: reducing the time users spend searching for movies;
- Verifying that the application remains stable, usable, and responsive during normal usage.

---

## 4. Test criteria

### Entry criteria

#### Website testing
- The website build is deployed and accessible for testing;
- Core pages and main user flows are available;
- Test data or test accounts are prepared.

#### API testing
- Required API endpoints are available for testing;
- API documentation or endpoint details are available;
- Test data and access permissions are prepared.

### Exit criteria

#### Website testing
- Main website functionality is tested;
- Critical and high-severity defects are fixed, retested, or accepted;
- No blocker prevents users from using core features.

#### API testing
- Main API endpoints are tested;
- Critical and high-severity defects are fixed, retested, or accepted;
- No blocker prevents correct API usage or integration with the website.

---

## 5. Resources Planning

### Man resources
- Product manager: Svitlana Drach
- Data analyst: Kseniia Kornienko
- Frontend developer: Tetiana Shabatura
- Backend developer: Karol Gajda
- QA Engineer: Shapoval Andrii

### System resource
- Jira – defect tracking, feature stories;
- TestRail – test case management and execution tracking;
- Chrome DevTools;
- Web browsers (Chrome, Edge);
- Postman (API Testing);
- Spreadsheet (for Test Summary metrics).

---

## 6. Test environment planning

### Operating system
- Windows 10 x64 Version: 22H2

### Supported Browsers (Latest)
- Google Chrome
- Microsoft Edge
- (MB OTHER ???????)

---

## 7. Schedule and Estimation
- Phase 1: Planning and Analysis — ?? m/h
- Phase 2: Test Design — ?? m/h
- Phase 3: Web application testing — ?? m/h
- Phase 5: Reporting and Closure — ?? m/h

---

## 8. Test Deliverables

### Before
- Test Plan;
- Decomposition;
- Test cases;
- Test data.

### During
- Permission testing table;
- RTM.

### After
- Test cases result;
- Bug reports;
- Test report (Test Summary).
