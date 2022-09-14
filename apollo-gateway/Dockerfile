FROM node:16-alpine

ARG NODE_ENV=production
ENV NODE_ENV=${NODE_ENV}

WORKDIR /usr/src/app

COPY package*.json ./
RUN npm install

COPY . .

CMD [ "npm", "run", "start"]