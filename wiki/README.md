# ๐ ์ธํด์ํค





- [MSA](#msa)
- [SRE](#sre)
- [Jenkins](#jenkins)
- [Git Flow](#git-flow)
- [Tunneling](#tunneling)
- [DocumentDB](#documentdb)
- [homebrew-cask](#homebrew-cask)
- [Swagger](#Swagger)
- [CI](#CI)
- [Replication VS Clustering](#Replication-VS-Clustering)


## MSA

### ๊ฐ๋
Micro Service Architecure

SOA(Service Oriented Architecture)์ ๊ฒฝ๋ํ ๋ฒ์ ์ผ๋ก Monolithic Architecture๋ฅผ ์ชผ๊ฐ์ ๋๋ฆฝ์ ์ผ๋ก ๊ตฌ๋ถํ๋ค. 

> Service: ํน์  ๊ธฐ๋ฅ์ ์งํฉ, service์ ๋ฒ์๊ฐ ์ค์

14๋ ์ด๋ฐ๋ถํฐ ํ์ฌ๊น์ง ์ฃผ๋ชฉ๋ฐ๊ณ  ์๋ค. ์ํคํ์ฒ ๊ด๋ จ ๊ธฐ์  ํ์ค์ ์์ผ๋ฉฐ SW ๋ฒค๋๋ค์ด ๊ธฐ์กด ์ ํ๊ตฐ(SOA, PaaS, ...)์ Microservices๋ฅผ ์ง์ํ๊ธฐ ์ํ ํ๋ซํผ์ผ๋ก์ rebrandํ๋ ์ถ์ธ๋ก ์ ํ๋๊ณ  ์๋ค.

### ๋ฐฐ๊ฒฝ: Monolithic ์ํคํ์ฒ์ ๋จ์ 

- ์ผ๋ถ ๋ชจ๋์ ๋ณ๊ฒฝ์ฌํญ ๋๋ฌธ์ ์ ์ฒด ์ดํ๋ฆฌ์ผ์ด์ ๊ฐ๋ฐ/์ด์ ํ๋ก์ธ์ค์ ํจํค์ง์ ์ํฅ์ ์ค๋ค.
- ๋ชจ๋๋ณ ํน์ฑ์ ๋ง๋ ์ ๊ธฐ์  ๋๋ ๊ตฌ์กฐ๋ฅผ ์ ์ฉํ๊ธฐ ์ด๋ ต๋ค.
- ๋ชจ๋๋ณ ํ์ฅ์ด ์ด๋ ต๋ค.

### ํน์ง

- ์ ํ๋ฆฌ์ผ์ด์ ๋ก์ง์ ๊ฐ์ ์ฑ์์ด ๋ชํํ ์์ ์ปดํฌ๋ํธ๋ค๋ก ๋ถํดํ๊ณ  ์ด๋ฅผ ์กฐํฉํด ์๋ฃจ์์ ์ ๊ณตํ๋ค.
- ๊ฐ ์ปดํฌ๋ํธ๋ ์์ ์ฑ์ ์์ญ์ ๋ด๋นํ๊ณ  ์์ ํ ์ํธ ๋๋ฆฝ์ ์ผ๋ก ๋ฐฐํฌ๋๋ค. ๋ง์ดํฌ๋ก ์๋น์ค๋ฅผ ๋น์ฆ๋์ค ์์ญ์ ํ ๋ถ๋ถ์์๋ง ์ฑ์์ ๋ด๋นํ๋ค. ๊ทธ๋ฆฌ๊ณ  ์ฌ๋ฌ ์ ํ๋ฆฌ์ผ์ด์์์ ์ฌ์ฌ์ฉํ  ์ ์์ด์ผ ํ๋ค.
- ์๋น์ค ์๋น์์ ์๋น์ค ์ ๊ณต์ ์ฌ์ด์ ๋ฐ์ดํฐ ๊ตํ์ ์ํด HTTP์ JSON๊ณผ ๊ฐ์ ๊ฒฝ๋ ํต์  ํ๋กํ ์ฝ์ ์ฌ์ฉํ๋ค.
- ์ ํ๋ฆฌ์ผ์ด์์ ํญ์ ๊ธฐ์  ์ค๋ฆฝ์  ํ๋กํ ์ฝ์ ์ฌ์ฉํด ํต์ ํ๋ฏ๋ก ์๋น์ค ๊ตฌํ ๊ธฐ์ ๊ณผ๋ ๋ฌด๊ดํ๋ค. ์ด๋ ๋ง์ดํฌ๋ก์๋น์ค ๊ธฐ๋ฐ์ ์ ํ๋ฆฌ์ผ์ด์์ ๋ค์ํ ์ธ์ด์ ๊ธฐ์ ๋ก ๊ตฌ์ถํ  ์ ์๋ค๋ ๊ฒ์ ์๋ฏธํ๋ค.
- ์๊ณ  ๋๋ฆฝ์ ์ด๋ฉฐ ๋ถ์ฐ๋ ๋ง์ดํฌ๋ก์๋น์ค๋ฅผ ์ฌ์ฉํด ์กฐ์ง์ ๋ชํํ ์ ์๋ ์ฑ์ ์์ญ์ ๋ด๋นํ๋ ์๊ท๋ชจ ํ์ ๋ณด์ ํ  ์ ์๋ค. ์ด ํ๋ค์ ์ ํ๋ฆฌ์ผ์ด์ ์ถ์์ฒ๋ผ ํ๋์ ๋ชฉํ๋ฅผ ํฅํด ์ผํ์ง๋ง, ์๊ธฐ๊ฐ ๊ฐ๋ฐํ๋ ์๋น์ค๋ง ์ฑ์์ง๋ค.

### ์ฅ์ 

- ์์ ์๋น์ค๋ค๋ก ๋๋๊ณ , ๊ฐ ์๋น์ค๋ฅผ ๋๋ฆฝ์ ์ผ๋ก ๋ง๋ฆ โ loosely-coupled
- ๋์ฉ๋ ๋ถ์ฐ ํ๊ฒฝ์ ์ ํฉ
- ๋ณต์ก๋ ๊ฐ์
- ์ ์ฐํ ๋ฐฐํฌ
- ์ฌ์ฌ์ฉ์ฑ โ ํ์ฅ์ฑ
- ์๋น์ค๋ณ HW/SW ํ๋ซํผ/๊ธฐ์  ๋์ ๋ฐ ํ์ฅ ์ฉ์ด
- ๊ฐ๋ฐ์์ ์ดํด ์ฉ์ด, ๊ฐ๋ฐ/์ด์ ๋งค ๋จ๊ณ์ ์์ฐ์ฑ ํฅ์
- ์ง์์ ์ธ ๊ฐ๋ฐ/๋ํ๋ก์ด๊ฐ biz capability ๋จ์๋ก ๊ด๋ จ๋ ์์์ ์ธ์์ ์ฑ์ ํ์ ์ด๋ฃจ์ด์ง ์ ์๋ค.
- fault isolation

### ๋จ์ 

- ์ฅ์ ์ถ์ , ๋ชจ๋ํฐ๋ง, ๋งค์์ง์ด ์ด๋ ต๋ค.
- ์ฌ๋ฌ ์๋น์ค์ ๊ฑธ์ณ์ ธ ์๋ feature์ ๊ฒฝ์ฐ, ํธ๋์ญ์์ ๋ค๋ฃจ๊ธฐ ์ด๋ ต๋ค.
- ์ฌ๋ฌ ์๋น์ค์ ๊ฑธ์ณ์ ธ ์๋ feature์ ๊ฒฝ์ฐ, ํ์คํ์ด ๋ณต์กํ๋ค.
- ์๋น์ค๊ฐ dependency๊ฐ ์๋ ๊ฒฝ์ฐ ๋ฆด๋ฆฌ์ฆ๊ฐ ๊น๋ค๋กญ๋ค.
- ์๋น์ค ๊ฐ์๊ฐ ๋ง๊ณ  ์ ๋์ ์ด๊ธฐ ๋๋ฌธ์ Continuous Integration/Delivery ๋ฐ ์๋น์ค ๊ด๋ฆฌ ์์ ๋ฌธ์ ๊ฐ ๋ฐ์ํ  ์ ์๋ค.
- monolith๋ก ์์ํ ์์คํ์ Microservices๋ก ์ ํํ  ๋ ํฐ ๊ณ ํต์ด ์๋ฐ๋  ์ ์๋ค.

---

## SRE

Site Reliability Engineering

> class SRE implements Devops

### ๋ฑ์ฅ ๋ฐฐ๊ฒฝ

- ๊ฐ๋ฐํ์ ์ฃผ์ด์ง ์๊ฐ ๋ด์ ์๋ก์ด ๊ธฐ๋ฅ์ ๋ด๊ธฐ ์ํด์ ๊ฐ๋ฐ ์๋์ ๋ฌด๊ฒ๋ฅผ ๋๊ณ , ์ด์ํ์ ๊ฒฝ์ฐ ์์คํ ์์ ์ฑ์ ๋ฌด๊ฒ๋ฅผ ๋๋ค.
- ๊ฐ๋ฐํ์ด ๋ฌด๋ฆฌํ๊ฒ ๊ธฐ๋ฅ์ ๋ฐฐํฌํ๋ฉด ์ฅ์ ๋ก ์ด์ด์ง๊ณ , ์ด๋ฌํ ์ฅ์ ๋ก ์ธํด ์๋ก๋ฅผ ์ํ๋ ์ํฉ์ด ๋ฐ์ํ๋ค. ๊ทธ๋์ Devops๋ ์ด๋ฌํ ๋ ํ์ ํ ํ์ ๋ฌถ์ด๋๊ณ  ์ด์ํ๋ ๋ฌธํ์ด์ ์ด์ ์ฒ ํ์ด๋ค.

### Devops์ SRE ์ฐจ์ด์ 

- Devops: ๊ฐ๋ฐ๊ณผ ์ด์์ ๋ถ๋จ ํ์์ ํด๊ฒฐํ๊ธฐ ์ํ ๋ฐฉ๋ฒ๋ก ์ด์ ํ๋์ ์กฐ์ง๋ฌธํ์ ๋ํ ๋ฐฉํฅ์ฑ์ด๋ค.
- SRE: ๊ตฌ๊ธ์ด Devops์ ์ ์ฉํ๊ธฐ ์ํ ๊ตฌ์ฒด์ ์ธ practice์ ๊ฐ์ด๋์ด๋ค.

### SRE๊ฐ ํ๋ ์ผ

- ์งํ ์ ์์ ๋ชจ๋ํฐ๋ง
- ํ๋์จ์ด ๋ฆฌ์์ค ๊ฐ์ฉ๋ ๊ณํ
- ํ์ ๋ณ๊ฒฝ ๊ด๋ฆฌ
- ์ฅ์  ์ฒ๋ฆฌ
- ๋ฌธํ ํ๋ฆฝ

### SRE๊ฐ ์ผํ๋ ๋ฐฉ์

- ๋ถ์ ๊ฐ ๋จ์  ์ค์ด๊ธฐ (์ค๋์ฝ ๊ณต์ )
- ์ ์์ ์ผ๋ก ์คํจ ๋ฐ์๋ค์ด๊ธฐ (์๋ฌ ๋ฒ์ง ๋ฑ์ ๊ฐ๋ ๋์)
- ์ ์ง์ ์ธ ๋ณ๊ฒฝ ๊ตฌํ (์์ ๋จ์ ๋ณ๊ฒฝ์ผ๋ก ๋ฆฌ์คํฌ ์ต์ํ)
- ์๋ํ ๋ฐ ํด๋ง (์์์์ ์ค์ฌ์ ๋ฆฌ์คํฌ ์ต์ํ ๋ฐ ์๋ฌด ์ ๊ฐ)
- ๋ชจ๋  ๊ฒ์ ์์นํ (์งํ ๋ฟ๋ง ์๋๋ผ, ์๋ ์์์๊ฐ, ์ฅ์ ์๊ฐ ๋ฑ ๋ชจ๋  ๊ฒ์ ๋ฐ์ดํฐํ)

### SRE์ ์ฃผ์ ์งํ

- SLI(Service Level Indicator): ์๋น์ค์ ๋ํ ์์ค์ ์ธก์ ํ์ฌ ์ ๋์ ์ผ๋ก ์ ์ํ ์งํ
    - ์๋ต์๊ฐ
    - ์๋ฌ์จ
    - ์ฒ๋ฆฌ๋
    - ๊ฐ์ฉ์ฑ
    - ๋ด๊ตฌ์ฑ
- SLO (Service Level Objective): SLI๋ก ์ ์ํ ์งํ์ ๋ชฉํ ์งํ
    - ์ต์/์ต๋ ๋ฒ์ ์ง์ 
    - ์ฌ์  ๊ฐ์ ๋ ๊ฒ
    - ๊ณผ๋ํ๊ฒ ํ์ง ๋ง ๊ฒ

---

## Jenkins

### ๊ฐ๋

์ํํธ์จ์ด ๊ฐ๋ฐ ์ ์ง์์ ์ธ ํตํฉ(continuous integration) ์๋น์ค๋ฅผ ์ ๊ณตํ๋ ํด์ด๋ค.

### ๋ฐฐ๊ฒฝ

์ด์ ์๋ ์ผ์  ์๊ฐ๋ง๋ค ๋น๋๋ฅผ ์คํํ๋ ๋ฐฉ์์ด ์ผ๋ฐ์ ์ด์๋ค. ํนํ ๊ฐ๋ฐ์๋ค์ด ๋น์ผ ์์ฑํ ์์ค๋ค์ ์ปค๋ฐ์ด ๋ชจ๋ ๋๋ ์ฌ์ผ ์๊ฐ๋์ ์ด๋ฌํ ๋น๋๊ฐ ํ์ด๋จธ์ ์ํด ์ง์ค์ ์ผ๋ก ์งํ๋์๋๋ฐ, ์ด๋ฅผ nightly-build๋ผ ํ๋ค. 

ํ์ง๋ง ์  ํจ์ค๋ ์ ๊ธฐ์ ์ธ ๋น๋์์ ํ ๋ฐ ๋์๊ฐ ์๋ธ ๋ฒ์ , Git๊ณผ ๊ฐ์ ๋ฒ์  ๊ด๋ฆฌ ์์คํ๊ณผ ์ฐ๋ํ์ฌ ์์ค์ ์ปค๋ฐ์ ๊ฐ์งํ๋ฉด ์๋์ ์ผ๋ก ์๋ํ ํ์คํธ๊ฐ ํฌํจ๋ ๋น๋๊ฐ ์๋๋๋๋ก ์ค์ ํ  ์ ์๋ค. 

### ์ฅ์ 

๊ฐ๋ฐ ์ค์ธ ํ๋ก์ ํธ์์ ์ปค๋ฐ์ ๋น๋ฒํ๊ฒ ์ผ์ด๋๊ธฐ ๋๋ฌธ์ ์ปค๋ฐ ํ์๋งํผ ๋น๋๋ฅผ ์คํํ๋ ๊ฒ์ด ์๋๋ผ ์์์ด ํ์๋์ด ์์ ์ด ์คํ๋  ์ฐจ๋ก๋ฅผ ๊ธฐ๋ค๋ฆฌ๊ฒ ๋๋ค. ์ฝ๋ ๋ณ๊ฒฝ๊ณผ ํจ๊ป ์ด๋ฃจ์ด์ง๋ ์ด ๊ฐ์ ์๋ํ๋ ๋น๋์ ํ์คํธ ์์๋ค์ ๋ค์๊ณผ ๊ฐ์ ์ด์ ์ ๊ฐ์ ธ๋ค์ค๋ค.

- ๊ฐ์ข ๋ฐฐ์น ์์์ ๊ฐ๋ตํ: DB ๊ตฌ์ถ, ์ ํ๋ฆฌ์ผ์ด์ ์๋ฒ๋ก์ Deploy, ๋ผ์ด๋ธ๋ฌ๋ฆฌ ๋ฆด๋ฆฌ์ฆ์ ๊ฐ์ด ์ด์ ์๋ CLI๋ก ์คํ๋๋ ์์์ด ๋๋ถ์ ์น ์ธํฐํ์ด์ค๋ก ์ฝ๊ฒ ๊ฐ๋ฅํด์ก๋ค.
- Build ์๋ํ์ ํ๋ฆฝ
- ๋น๋ ํ์ดํ๋ผ์ธ ๊ตฌ์ฑ
- ์๋ํ ํ์คํธ: ์  ํจ์ค๋ Subversion์ด๋ Git๊ณผ ๊ฐ์ ๋ฒ์  ๊ด๋ฆฌ ์์คํ๊ณผ ์ฐ๋ํ์ฌ ์ฝ๋ ๋ณ๊ฒฝ์ ๊ฐ์งํ๊ณ  ์๋ํ ํ์คํธ๋ฅผ ์ํํ๊ธฐ ๋๋ฌธ์ ๊ฐ์ธ์ด ๋ฏธ์ฒ ์ค์ํ์ง ๋ชปํ ํ์คํธ๊ฐ ์๋ค ํด๋ ๋ ๋ ํ ์์ ๋ง์ด ๋์ด์ค๋ค.
- ์ฝ๋ ํ์ค ์ค์ ์ฌ๋ถ ๊ฒ์ฌ

---

## Git Flow

### ๊ฐ๋

- ์ผ๋ฐ์ ์ธ ๊ฒฝ์ฐ์ ์ ์ ํ๊ณ  ํจ์จ์ ์ธ **ํ์ ๊ด๋ฆฌ ์ ๋ต**์ด๋ค.

    ์ผ๋ฐ์ ์ธ ๊ฒฝ์ฐ์ ํจ์จ์ ์ด๊ณ  ๋ณํ์ ์ ์ฐํ๊ฒ ๋์ฒํ  ์ ์๊ฒ ํ๋ ์๋ฒ ํ๋ก๊ทธ๋จ ๊ตฌ์ฑ์ ๋ํ MVC ํจํด๊ณผ ๊ฐ๋ค.

- **์์ค ์ฝ๋ ํ์/์ด๋ ฅ ๊ด๋ฆฌ๋ฅผ ํจ์จ์ ์ผ๋ก ํ๊ณ  ํ์ํ  ๋ ๋ฐ์ํ  ์ ์๋ ๋ฌธ์ ์ ์ ์ต์ํ**ํ  ์ ์๋ **์ ๋ต**์ ๋งํ๋ค.

![image](https://user-images.githubusercontent.com/50407047/111655742-3cde6800-884d-11eb-867f-d92e75449054.png)

### ๊ฐ branch

- **master branch**
    - **๋ฐฐํฌ**๋์๊ฑฐ๋ ๋ฐฐํฌ๋  ์์ค๊ฐ ์ ์ฅ๋๋ ๋ธ๋์น
    - ๋ฐฐํฌ๊ฐ ๋  ๋๋ง๋ค ํ๊ทธ๋ง ๋ฌ์์ฃผ๋ ํ์์ผ๋ก ๊ด๋ฆฌํ๋ค.
    - ์ธ์ ๋  ์ํ๋ ๋ฒ์ ์ ์์ค๋ฅผ ๋ฐ์๋ณผ ์ ์๊ฒ ํ๋ ์ญํ ์ ํ๋ค.
- **develop branch**
    - ๋ค์ ๋ฐฐํฌ๋ฅผ ์ํด **๊ฐ๋ฐ**์ ์งํํ๋ ๋ธ๋์น
    - ์ฌ๋ฌ ๋ช์ ๊ฐ๋ฐ์๊ฐ ํจ๊ป ๊ณต์ ํ๋ฉด์ ๊ฐ๋ฐ์ ์งํํ๋ ๋ธ๋์น
- **feature branch**
    - ๊ฐ ๊ฐ๋ฐ์์ ์ํด **๊ธฐ๋ฅ ๋จ์ ๊ฐ๋ฐ**์ด ์งํ๋๋ ๋ธ๋์น
    - ๊ฐ๋ฐ์๋ค์ด ๊ฐ๊ฐ ๊ฐ๋ฐํ๋ ๊ธฐ๋ฅ์ ๊ฐ๋ฐํ๊ธฐ ์ ์ remote repository์ develop branch๋ก๋ถํฐ ์์ ์ ๋ก์ปฌ์ ๋ธ๋์น๋ฅผ ๋ฐ๋ก ์์ฑํด์ ๊ฐ๋ฐ์ ์งํํ๊ณ , ๋ก์ปฌ ๋ธ๋์น์์ ๊ฐ๋ฐ์ด ์๋ฃ๋๋ฉด ์๋ฃ๋ ์์ค๋ฅผ develop ๋ธ๋์น์ ํธ์ํ๊ฑฐ๋ PullRequest๋ฅผ ๋ณด๋ด์ ๋ด๋ถ์ ์ธ ์ฝ๋ ๋ฆฌ๋ทฐ ํ์ mergeํ๋ ๊ฒ์ ํตํด ๊ฐ๋ฐ์ด ์งํ๋๋ค.
    - ex) ํ์๊ฐ์ ๊ธฐ๋ฅ์ ๊ฐ๋ฐํด์ผ ํ๋ค๋ฉด remote repository์ develop ๋ธ๋์น๋ก๋ถํฐ ํ์๊ฐ์ ๊ธฐ๋ฅ์ ์ํ ๋ธ๋์น๋ฅผ ๋ก์ปฌ์ ์์ฑํด์ ์งํํ๋ค. ์ค๊ฐ์ "domain ์์ฑ", "db ์ฐ๋", "์ํธํ ์ ์ฉ" ๋ฑ์ commit ๋ฉ์์ง๋ฅผ ๋จ๊ธธ ๊ฒ์ด๊ณ , ์๋ฃ๋๋ฉด remote repository์ develop ๋ธ๋์น๋ก merge ๋๊ณ  ์ด ๋ธ๋์น๋ ์ฌ๋ผ์ง๋ค.
- **hotfixs branch**
    - ๋ฐฐํฌ ๋ฒ์ ์ ์๊ธด ๋ฌธ์ ๋ก **๊ธด๊ธํ ํธ๋ฌ๋ธ์ํ**์ด ํ์ํ  ๋ ๊ฐ๋ฐ์ด ์งํ๋๋ ๋ธ๋์น
- **release branch**
    - **๋ด๋ถ์ ์ผ๋ก ๋ฐฐํฌํ  ์ค๋น**๊ฐ ๋์๋ค๊ณ  ์๊ฐํ๋ ์์ค๊ฐ ์ ์ฅ๋๋ ๋ธ๋์น

---

## Tunneling

### ๊ฐ๋

- ๋ฐ์ดํฐ ์คํธ๋ฆผ์ ์ธํฐ๋ท ์์์ ๊ฐ์์ ํ์ดํ๋ฅผ ํตํด ์ ๋ฌ์ํค๋ ๊ธฐ์ 
- ์ปดํจํฐ ๋คํธ์ํฌ์์ ํฐ๋๋ง ํ๋กํ ์ฝ์ ์ฌ์ฉํ๋ฉด ๋คํธ์ํฌ ์ฌ์ฉ์๋ ๊ธฐ๋ณธ ๋คํธ์ํฌ๊ฐ ์ง์  ์ ๊ณตํ์ง ์๋ ๋คํธ์ํฌ ์๋น์ค์ ์ ๊ทผํ๊ฑฐ๋ ์ ๊ณตํ  ์ ์๋ค.

### ํน์ง

- ํธ์คํธ์ ํธ์คํธ ์ฌ์ด์ ํต์ ์ด ์ด๋ค ํ๋กํ ์ฝ์ ์ฌ์ฉํ๋ ๊ฐ์ ์ผ๋จ ํฐ๋์ด ๊ตฌ์ฑ๋๋ฉด ๊ทธ ์์ ์๋ ๋ด์ฉ๋ฌผ(ํจํท)์ ๊ฐ์ธ์ง๊ธฐ(์บก์ํ) ๋๋ฌธ์ ๋ด์ฉ๋ฌผ์ด ๋ฌด์์ธ๊ฐ๋ ์ค์ํ์ง ์๊ฒ ๋๋ค.

### ์บก์ํ์ ํฐ๋๋ง

- ์บก์ํ๋ OSI 7 ๊ณ์ธต์ ์ฐธ์กฐํ์ฌ ํต์ ์ ํ๊ธฐ ์ํด ํ์ ๊ณ์ธต์์ ์์ ๊ณ์ธต ๋ฐ์ดํฐ๋ฅผ ํฌ์ฅํ๋ ๊ฐ๋์ด๋ค.
- ํฐ๋๋ง์ ๊ณ์ธต์ด ๋์ผํ๊ฑฐ๋ ํ์์ ๋ค๋ฅธ ํ๋กํ ์ฝ์ ์จ๊ธฐ๊ธฐ ์ํด ์ฌ์ฐ์ด์์ ๋ฐ์ดํฐ๋ฅผ ์บก์ํํ๊ณ , ์ธ๋ถ ๋คํธ์ํฌ๋ฅผ ํต๊ณผํ์ฌ ๋ชฉ์ ์ง์ ๋์ฐฉํ ๋ค ๋ค์ ๋์ํ(Decapsulation)๊น์ง ํ๋ ๋ชจ๋  ํต์  ๊ณผ์ ์ ์ผ์ปซ๋ ๋ง์ด๋ค.

### ๊ตฌ์ฑ์์

- Passenger(์น๊ฐ) ํ๋กํ ์ฝ: ์บก์ํ๊ฐ ๋์ด์ผ ํ  ํ๋กํ ์ฝ๋ค
- Carrier(์ ๋ฌ) ํ๋กํ ์ฝ: ์บก์ํ ์ํฌ ํ๋กํ ์ฝ
- Transport(์ ์ก) ํ๋กํ ์ฝ: ์ ๋ฌ ํ๋กํ ์ฝ์ ๋๊ณ  ๊ฐ ํ๋กํ ์ฝ

### ๊ณผ์ 

๋ด๋ถ ๋คํธ์ํฌ์์๋ง ์ฌ์ฉ๋๋ Passenger ํ๋กํ ์ฝ์ Carrier ํ๋กํ ์ฝ์ ๋ฐ์ดํฐ ์์ผ๋ก ์ง์ด๋ฃ์ ๋ค Transport ํ๋กํ ์ฝ์ ํตํด ๋ชฉ์ ์ง๋ก ์ด๋์ํจ๋ค. ๊ทธ๋ฆฌ๊ณ  ๋ชฉ์ ์ง์ ๋์ฐฉํ๋ฉด ๋์บก์ํํด Carrier ํ๋กํ ์ฝ ์์ ๋ค์ด์๋ Passenger ์ ๋์์์ผ์ ์ํ๋ ๋ชฉ์ ์ง๋ก ์ด๋ํ๋ค.

---

## DocumentDB

- AWS์์ ์ ๊ณตํ๋ NoSQL ๊ธฐ๋ฐ์ ๋ฐ์ดํฐ๋ฒ ์ด์ค ์๋น์ค์ด๋ค.
- MongoDB์ ๊ธฐ๋ฅ์ ์ผ๋ก ํฐ ์ฐจ์ด๊ฐ ์๋ค.

---

## homebrew-cask

Homebrew์ ํ์ฅ์ด๋ค. Homebrew๋ ํจํค์ง๋ฅผ ํฐ๋ฏธ๋(์)์์ command๋ฅผ ์๋ ฅํ๋ ๊ฒ๋ง์ผ๋ก๋ ์ค์นํด์ค๋ค. Homebrew-cask๋ GUI ์ฆ ๋งฅ์ ์์ฉํ๋ก๊ทธ๋จ์ ์ฝ๋งจ๋๋ก ์ค์นํ  ์ ์๋๋ก ํด์ฃผ๋ ํธ๋ฆฌํ ๊ธฐ๋ฅ์ด๋ค.

---

## Swagger

- API ๋ช์ธ๋ฅผ ๋์์ฃผ๋ ๋๊ตฌ์ด๋ค.
- ๋ฐฑ์๋ ๊ฐ๋ฐ์์ ํ๋ก ํธ์๋ ๊ฐ๋ฐ์๊ฐ ์๋ก ํ๋ ฅํ๋ ํํ๋ก ๊ฐ๋ฐ์ ์งํํ๊ฒ ๋๋ค. ์ด๋ ๋ฐฑ์๋ ํ๋ก๊ทธ๋จ๊ณผ ํ๋ก ํธ์๋ ํ๋ก๊ทธ๋จ ์ฌ์ด์์ ์ ํํ ์ด๋ค ๋ฐฉ์์ผ๋ก ๋ฐ์ดํฐ๋ฅผ ์ฃผ๊ณ ๋ฐ์ ์ง์ ๋ํ ๋ช์ธ๊ฐ ํ์ํ๋ค. ์ด๋ฐ ๋ด์ฉ์ด ๋ด๊ธด ๋ฌธ์๋ฅผ API ๋ช์ธ์๋ผ๊ณ  ํ๋ค.

## Replication

- Master ๋ธ๋์ ์ฐ๊ธฐ ํธ๋์ญ์์ด ์ํ๋๋ค.
- Master ๋ธ๋๋ ๋ฐ์ดํฐ๋ฅผ ์ ์ฅํ๊ณ  ํธ๋์ญ์์ ๋ํ ๋ก๊ทธ๋ฅผ ํ์ผ์ ๊ธฐ๋กํ๋ค.
- Slave ๋ธ๋์ IO Thread๋ Master ๋ธ๋์ ๋ก๊ทธ ํ์ผ(BIN LOG)๋ฅผ ํ์ผ(Replay Log)์ ๋ณต์ฌํ๋ค.
- Slave ๋ธ๋์ SQL Thread๋ ํ์ผ(Replay Log)๋ฅผ ํ ์ค์ฉ ์ฝ์ผ๋ฉฐ ๋ฐ์ดํฐ๋ฅผ ์ ์ฅํ๋ค.

๋ฆฌํ๋ฆฌ์ผ์ด์์ Master์ Slave ๊ฐ์ ๋ฐ์ดํฐ ๋ฌด๊ฒฐ์ฑ ๊ฒ์ฌ(๋ฐ์ดํฐ๊ฐ ์ผ์นํ๋์ง)๋ฅผ ํ์ง ์๋ ๋น๋๊ธฐ ๋ฐฉ์์ผ๋ก ๋ฐ์ดํฐ๋ฅผ ๋๊ธฐํํ๋ค. 

### Replication์ ์ฅ์ ๊ณผ ๋จ์ 

- ์ฅ์ 
  - DB์์ฒญ์ 60~80% ์ ๋๊ฐ ์ฝ๊ธฐ ์์์ด๊ธฐ ๋๋ฌธ์ Replication๋ง์ผ๋ก๋ ์ถฉ๋ถํ ์ฑ๋ฅ์ ๋์ผ ์ ์๋ค.
  - **๋น๋๊ธฐ ๋ฐฉ์**์ผ๋ก ์ด์๋์ด ์ง์ฐ ์๊ฐ์ด ๊ฑฐ์ ์๋ค.
- ๋จ์ 
  - ๋ธ๋๋ค ๊ฐ์ ๋ฐ์ดํฐ ๋๊ธฐํ๊ฐ ๋ณด์ฅ๋์ง ์์ ์ผ๊ด์ฑ์๋ ๋ฐ์ดํฐ๋ฅผ ์ป์ง ๋ชปํ  ์ ์๋ค.
  - Master ๋ธ๋๊ฐ ๋ค์ด๋๋ฉด ๋ณต๊ตฌ ๋ฐ ๋์ฒ๊ฐ ๊น๋ค๋กญ๋ค.

## Clustering

- ์ฌ๋ฌ ๊ฐ์ DB๋ฅผ ์ํ์ ์ธ ๊ตฌ์กฐ๋ก ๊ตฌ์ถํ๋ ๋ฐฉ์์ด๋ค.
- ๋ถ์ฐ ํ๊ฒฝ์ ๊ตฌ์ฑํ์ฌ Single Point of Failure์ ๊ฐ์ ๋ฌธ์ ๋ฅผ ํด๊ฒฐํ  ์ ์๋ Fail Over ์์คํ์ ๊ตฌ์ถํ๊ธฐ ์ํด์ ์ฌ์ฉ๋๋ค.
- ํด๋ฌ์คํฐ๋ง์ ๋๊ธฐ ๋ฐฉ์์ผ๋ก ๋ธ๋๋ค ๊ฐ์ ๋ฐ์ดํฐ๋ฅผ ๋๊ธฐํํ๋ค.

### Clustering์ ์ฒ๋ฆฌ ๋ฐฉ์

1. 1๊ฐ์ ๋ธ๋์ ์ฐ๊ธฐ ํธ๋์ญ์์ด ์ํ๋๊ณ , COMMIT ์ ์คํํ๋ค.
2. ์ค์  ๋์คํฌ์ ๋ด์ฉ์ ์ฐ๊ธฐ ์ ์ ๋ค๋ฅธ ๋ธ๋๋ก ๋ฐ์ดํฐ์ ๋ณต์ ๋ฅผ ์์ฒญํ๋ค.
3. ๋ค๋ฅธ ๋ธ๋์์ ๋ณต์  ์์ฒญ์ ์๋ฝํ๋ค๋ ์ ํธ(OK)๋ฅผ ๋ณด๋ด๊ณ , ๋์คํฌ์ ์ฐ๊ธฐ๋ฅผ ์์ํ๋ค.
4. ๋ค๋ฅธ ๋ธ๋๋ก๋ถํฐ ์ ํธ(OK)๋ฅผ ๋ฐ์ผ๋ฉด ์ค์  ๋์คํฌ์ ๋ฐ์ดํฐ๋ฅผ ์ ์ฅํ๋ค.

### Clustering์ ์ฅ์ ๊ณผ ๋จ์ 

- ์ฅ์ 
  - ๋ธ๋๋ค ๊ฐ์ ๋ฐ์ดํฐ๋ฅผ ๋๊ธฐํํ์ฌ ํญ์ ์ผ๊ด์ฑ ์๋ ๋ฐ์ดํฐ๋ฅผ ์ป์ ์ ์๋ค.
  - 1๊ฐ์ ๋ธ๋๊ฐ ์ฃฝ์ด๋ ๋ค๋ฅธ ๋ธ๋๊ฐ ์ด์ ์์ด ์์คํ์ ๊ณ์ ์ฅ์  ์์ด ์ด์ํ  ์ ์๋ค.
- ๋จ์ 
  - ์ฌ๋ฌ ๋ธ๋๋ค ๊ฐ์ ๋ฐ์ดํฐ๋ฅผ ๋๊ธฐํํ๋ ์๊ฐ์ด ํ์ํ๋ฏ๋ก Replication์ ๋นํด ์ฐ๊ธฐ ์ฑ๋ฅ์ด ๋จ์ด์ง๋ค.
  - ์ฅ์ ๊ฐ ์ ํ๋ ๊ฒฝ์ฐ ์ฒ๋ฆฌ๊ฐ ๊น๋ค๋ก์ฐ๋ฉฐ, ๋ฐ์ดํฐ ๋๊ธฐํ์ ์ํด ์ค์ผ์ผ๋ง์ ํ๊ณ๊ฐ ์๋ค.

### Clustering์ ๊ตฌํํ๋ ๋ฐฉ๋ฒ

- Active-Active: ํด๋ฌ์คํฐ๋ฅผ ํญ์ ๊ฐ๋ํ์ฌ ๊ฐ์ฉ ๊ฐ๋ฅํ ์ํ๋ก ๋๋ ๊ตฌ์ฑ ๋ฐฉ์
- Active-Standby: ์ผ๋ถ ํด๋ฌ์คํฐ๋ ๊ฐ๋ํ๊ณ , ์ผ๋ถ ํด๋ฌ์คํฐ๋ ๋๊ธฐ ์ํ๋ก ๊ตฌ์ฑํ๋ ๋ฐฉ์