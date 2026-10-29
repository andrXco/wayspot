import admin from "firebase-admin";
import { faker } from "@faker-js/faker";
// Para subir al Firebase REAL: descomenta la siguiente línea después de poner serviceAccountKey.json en esta carpeta
// import serviceAccount from "./serviceAccountKey.json" with { type: "json" };

admin.initializeApp({
  // --- MODO FIREBASE REAL (producción) ---
  // credential: admin.credential.cert(serviceAccount),

  // --- MODO FIREBASE CLI (emulador local) ---
  projectId: "wayspot-60fd2",
});

const db = admin.firestore();

const PLACE_CATEGORIES = [
  "Playa",
  "Montaña",
  "Museo",
  "Parque",
  "Naturaleza",
  "Ciudad",
  "Patrimonio",
];

function buildUser(i) {
  const firstName = faker.person.firstName();
  const lastName = faker.person.lastName();
  const username = `${firstName}_${lastName}`.toLowerCase();
  const email = `${username}@sample.com`;

  return {
    id: `${i}`,
    name: `${firstName} ${lastName}`,
    username,
    email,
    fcmToken: "",
    country: faker.location.country(),
    bio: faker.lorem.sentence(),
    avatarUrl: `https://picsum.photos/seed/user${i}/150/150`,
    followersCount: faker.number.int({ min: 0, max: 5000 }),
    followingCount: faker.number.int({ min: 0, max: 5000 }),
    notificationPreferences: {
      newFollowers: faker.datatype.boolean(),
      reviewComments: faker.datatype.boolean(),
      likesReceived: faker.datatype.boolean(),
    },
  };
}

function buildPlace(i) {
  return {
    id: `${i}`,
    title: faker.location.city(),
    category: faker.helpers.arrayElement(PLACE_CATEGORIES),
    location: `${faker.location.city()}, ${faker.location.country()}`,
    rating: Number(
      faker.number.float({ min: 3.0, max: 5.0, fractionDigits: 1 })
    ),
    imageUrl: `https://picsum.photos/seed/place${i}/400/300`,
    description: faker.lorem.paragraph(),
    reviewCount: faker.number.int({ min: 0, max: 50 }),
  };
}

function buildReview(i, user, place) {
  return {
    id: `${user.id}_${i}`,
    rating: faker.number.int({ min: 1, max: 5 }),
    title: faker.lorem.sentence({ min: 3, max: 6 }),
    description: faker.lorem.words({ min: 5, max: 40 }),
    userId: user.id,
    placeId: place.id,
    reviewDate: faker.date
      .between({ from: "2024-01-01", to: new Date() })
      .toISOString()
      .split("T")[0],
    authorName: user.name,
    user: {
      name: user.name,
      username: user.username,
      avatarUrl: user.avatarUrl,
    },
    likesCount: faker.number.int({ min: 0, max: 500 }),
    liked: faker.datatype.boolean(),
  };
}

async function seedUsers(count = 5) {
  const batch = db.batch();
  const users = [];

  for (let i = 1; i <= count; i++) {
    const user = buildUser(i);
    users.push(user);
    const userRef = db.collection("users").doc(user.id);
    batch.set(userRef, user);
  }

  await batch.commit();
  return users;
}

async function seedPlaces(count = 5) {
  const batch = db.batch();
  const places = [];

  for (let i = 1; i <= count; i++) {
    const place = buildPlace(i);
    places.push(place);
    const placeRef = db.collection("places").doc(place.id);
    batch.set(placeRef, place);
  }

  await batch.commit();
  return places;
}

async function seedReviews(users, places, reviewsPerUser = 2) {
  const batch = db.batch();

  for (const user of users) {
    for (let i = 1; i <= reviewsPerUser; i++) {
      const place = faker.helpers.arrayElement(places);
      const review = buildReview(i, user, place);
      const reviewRef = db.collection("reviews").doc(review.id);
      batch.set(reviewRef, review);
    }
  }

  await batch.commit();
}

async function main() {
  try {
    const users = await seedUsers(5);
    const places = await seedPlaces(5);
    await seedReviews(users, places, 2);
    console.log("Datos cargados correctamente.");
  } catch (error) {
    console.error(error);
  }
}

main();

