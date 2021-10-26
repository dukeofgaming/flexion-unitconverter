ARG NGINX_VERSION

FROM nginx:$NGINX_VERSION

#COPY ./etc/nginx/* /etc/nginx/
COPY ./etc/nginx/conf.d/* /etc/nginx/conf.d/
COPY ./etc/ssl/certs/* /etc/ssl/certs/
