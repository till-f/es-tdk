#ifndef APP_H
#define APP_H

void etimer00ISR();
void etimer01ISR();
void etimer02ISR();
void SwIrq3ISR();

void init_app();
void run_app();

#endif
