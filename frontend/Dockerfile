FROM node:19-alpine

WORKDIR /app

COPY package*.json ./

RUN npm install\
    && npm install typescript -g

COPY . .

RUN npm run build

EXPOSE 3000

CMD ["npm", "start"]