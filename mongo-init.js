db.getSiblingDB('blackjack').createUser({
  user: 'blackjack_user',
  pwd: 'blackjack_mongo_pass',
  roles: [{
    role: 'readWrite',
    db: 'blackjack'
  }]
});